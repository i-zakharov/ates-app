package ru.zim.ates.tasktracker.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.zim.ates.common.consumer.BaseConsumer;
import ru.zim.ates.common.exception.AppException;
import ru.zim.ates.common.schemaregistry.EventEnvelope;
import ru.zim.ates.common.schemaregistry.EventSchemaRegistry;
import ru.zim.ates.common.schemaregistry.utils.Utils;

@Service
public class TasksPricesConsumer extends BaseConsumer {
    @Autowired
    private EventSchemaRegistry eventSchemaRegistry;
    @Autowired
    private TaskService taskService;

    @Autowired
    public TasksPricesConsumer(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @SneakyThrows
    @Override
    protected void processMessage(String message) {
        EventEnvelope eventEnvelope = eventSchemaRegistry.parseAndValidate(message);
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
