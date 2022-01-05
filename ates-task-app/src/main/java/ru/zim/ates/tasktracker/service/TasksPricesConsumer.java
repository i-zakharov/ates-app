package ru.zim.ates.tasktracker.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zim.ates.common.application.exception.AppException;
import ru.zim.ates.common.messaging.consumer.PersistEventConsumer;
import ru.zim.ates.common.messaging.schemaregistry.EventEnvelope;
import ru.zim.ates.common.messaging.schemaregistry.EventSchemaRegistry;
import ru.zim.ates.common.messaging.utils.Utils;

@Service
public class TasksPricesConsumer extends PersistEventConsumer {
    @Autowired
    private EventSchemaRegistry eventSchemaRegistry;
    @Autowired
    private TaskService taskService;

    @SneakyThrows
    @Override
    protected void processMessage(EventEnvelope eventEnvelope) {
        switch (eventEnvelope.getEventType()) {
            case ATES_TASK_PRICE_SET:
                onTaskPriceSet(eventEnvelope);
                break;
            default:
                throw new AppException(String.format("Unexpect event type: %s", eventEnvelope.getEventType()));
        }

    }

    @SneakyThrows
    private void onTaskPriceSet(EventEnvelope eventEnvelope) {
        Map<String, Object> eventFieldsMap = Utils.mapper.readValue(eventEnvelope.getData().toString(), HashMap.class);
        String publicId = eventFieldsMap.get("publicId").toString();
        BigDecimal assignePrice = new BigDecimal(eventFieldsMap.get("assignePrice").toString());
        BigDecimal closePrice = new BigDecimal(eventFieldsMap.get("closePrice").toString());
        taskService.setPrice(UUID.fromString(publicId), assignePrice, closePrice);
    }
}
