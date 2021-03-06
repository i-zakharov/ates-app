package ru.zim.ates.common.messaging.schemaregistry;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.zim.ates.common.messaging.config.AppEventType;
import ru.zim.ates.common.messaging.config.AppEventTypeProvider;
import ru.zim.ates.common.messaging.schemaregistry.exception.EventValidationException;
import ru.zim.ates.common.messaging.schemaregistry.exception.EventVersionNotFoundException;
import ru.zim.ates.common.messaging.utils.Utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.zim.ates.common.messaging.config.MqConfig.ATES_AUTH_PRODUCER_NAME;

public class RegistryTest {

    private static EventSchemaRegistry eventSchemaRegistry;

    @BeforeAll
    @SneakyThrows
    static void init() {
        eventSchemaRegistry = new EventSchemaRegistry(new AppEventTypeProvider());
        eventSchemaRegistry.init();
    }

    @Test
    @SneakyThrows
    void initResultTest() {
        eventSchemaRegistry.getJsonSchema(AppEventType.ATES_TEST, "1");
        assertThrows(EventVersionNotFoundException.class, () -> eventSchemaRegistry.getJsonSchema(AppEventType.ATES_TEST, "1000"));
    }

    @Test
    @SneakyThrows
    void validationTest() {
        String testMsgExample;
        EventEnvelope envelopeExample;

        testMsgExample = readResource("schemaregistry/test/ates-test-exchange/ATES_TEST/example1.json");
        envelopeExample = eventSchemaRegistry.parseAndValidate(testMsgExample);
        assertEquals(envelopeExample.getEventType(), AppEventType.ATES_TEST);

        String testMsgExample2 = readResource("schemaregistry/test/ates-test-exchange/ATES_TEST/example2.json");
        Throwable e = assertThrows(EventValidationException.class, () -> eventSchemaRegistry.parseAndValidate(testMsgExample2));
        System.out.println(e.toString());
    }

    @Test
    @SneakyThrows
    void serializationTest() {
        EventEnvelope eventEnvelope = EventEnvelope.preSetBuilder()
                .eventType(AppEventType.ATES_TEST)
                .eventVersion("0")
                .producer(ATES_AUTH_PRODUCER_NAME)
                .data("Some text")
                .build();
        String rawMessage = Utils.mapper.writeValueAsString(eventEnvelope);
        EventEnvelope parcedEventEnvelope = eventSchemaRegistry.parseAndValidate(rawMessage);
        parcedEventEnvelope.setData(Utils.mapper.readValue(parcedEventEnvelope.getData().toString(), String.class));
        assertEquals(eventEnvelope, parcedEventEnvelope);
    }

    private String readResource(String name) throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(name)) {
            StringWriter writer = new StringWriter();
            IOUtils.copy(inputStream, writer, StandardCharsets.UTF_8);
            return writer.toString();
        }
    }

}
