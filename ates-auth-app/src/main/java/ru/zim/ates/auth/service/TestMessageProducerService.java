package ru.zim.ates.auth.service;

import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zim.ates.common.messaging.config.AppEventType;
import ru.zim.ates.common.messaging.schemaregistry.EventEnvelope;
import ru.zim.ates.common.messaging.schemaregistry.EventSchemaRegistry;
import ru.zim.ates.common.messaging.utils.Utils;

import static ru.zim.ates.common.messaging.config.MqConfig.ATES_AUTH_PRODUCER_NAME;
import static ru.zim.ates.common.messaging.config.MqConfig.ATES_TEST_EXCHANGE;
import static ru.zim.ates.common.messaging.config.MqConfig.ATES_TEST_ROOTING_KEY;

@Service
public class TestMessageProducerService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private EventSchemaRegistry eventSchemaRegistry;

    @SneakyThrows
    public void sendMessage(MessageDto message) {
        EventEnvelope eventEnvelope = EventEnvelope.preSetBuilder()
                .eventType(AppEventType.ATES_TEST)
                .eventVersion("1")
                .producer(ATES_AUTH_PRODUCER_NAME)
                .data(message).build();

        String rawMessage = Utils.mapper.writeValueAsString(eventEnvelope);
        eventSchemaRegistry.parseAndValidate(rawMessage);

        rabbitTemplate.convertAndSend(ATES_TEST_EXCHANGE,
                ATES_TEST_ROOTING_KEY, rawMessage
        );
    }

    @Data
    public static class MessageDto {
        String text;
    }

}
