package ru.zim.ates.tasktracker.service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.zim.ates.common.schemaregistry.EventEnvelope;
import ru.zim.ates.common.schemaregistry.EventSchemaRegistry;
import ru.zim.ates.common.schemaregistry.utils.Utils;

@Component
@Slf4j
public class ConsumerService {
    @Autowired
    private EventSchemaRegistry eventSchemaRegistry;

    private CountDownLatch latch = new CountDownLatch(1);

    @SneakyThrows
    public void receiveMessage(String message) {
        log.info("Received {}", message);
        EventEnvelope eventEnvelope = eventSchemaRegistry.parseAndValidate(message);
        if ("1".equals(eventEnvelope.getEventVersion())) {
            TestDto testDto = Utils.mapper.readValue(eventEnvelope.getData().toString(), TestDto.class);
            log.info("Received Test Dto {}", testDto);
        }

        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TestDto {
        private String text;
    }

}
