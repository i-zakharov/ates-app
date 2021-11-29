package ru.zim.ates.billing.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.zim.ates.common.consumer.BaseConsumer;
import ru.zim.ates.common.exception.AppException;
import ru.zim.ates.common.producer.ProducerNotifyEvent;
import ru.zim.ates.common.schemaregistry.EventEnvelope;
import ru.zim.ates.common.schemaregistry.EventSchemaRegistry;
import ru.zim.ates.common.schemaregistry.EventType;
import ru.zim.ates.common.schemaregistry.MqConfig;
import ru.zim.ates.common.schemaregistry.utils.Utils;

@Service
public class TasksConsumer extends BaseConsumer {
    @Autowired
    private EventSchemaRegistry eventSchemaRegistry;
    @Autowired
    private BillingFacadeService billingFacadeService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public TasksConsumer(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @SneakyThrows
    @Override
    protected void processMessage(String message) {
        EventEnvelope eventEnvelope = eventSchemaRegistry.parseAndValidate(message);
        switch (eventEnvelope.getEventType()) {
            case ATES_TASK_PENDING:
                onTaskPending(eventEnvelope);
                break;
            case ATES_TASK_ASSIGNED:
                onTaskAssigned(eventEnvelope);
                break;
            case ATES_TASK_CLOSED:
                onTaskClosed(eventEnvelope);
                break;
            default:
                break; //Пока игнорируем неизвестные сообщения
                //throw new AppException(String.format("Unexpect event type: %s", eventEnvelope.getEventType()));
        }

    }

    @SneakyThrows
    private void onTaskClosed(EventEnvelope eventEnvelope) {
        assertVersion(eventEnvelope, "1");
        Map<String, Object> eventFieldsMap = Utils.mapper.readValue(eventEnvelope.getData().toString(), HashMap.class);
        String taskPublicId = eventFieldsMap.get("taskPublicId").toString();
        String assigneePublicId = eventFieldsMap.get("assigneePublicId").toString();
        billingFacadeService.closeTask(UUID.fromString(taskPublicId), UUID.fromString(assigneePublicId));
    }

    @SneakyThrows
    private void onTaskAssigned(EventEnvelope eventEnvelope) {
        assertVersion(eventEnvelope, "1");
        Map<String, Object> eventFieldsMap = Utils.mapper.readValue(eventEnvelope.getData().toString(), HashMap.class);
        String taskPublicId = eventFieldsMap.get("taskPublicId").toString();
        String assigneePublicId = eventFieldsMap.get("assigneePublicId").toString();
    }

    @SneakyThrows
    private void onTaskPending(EventEnvelope eventEnvelope) {
        assertVersion(eventEnvelope, "1");
        Map<String, Object> eventFieldsMap = Utils.mapper.readValue(eventEnvelope.getData().toString(), HashMap.class);
        String publicId = eventFieldsMap.get("publicId").toString();
        PricingService.TaskPrices prices = billingFacadeService.calculateAndSetTaskPrice(UUID.fromString(publicId));
        sendTaskPriceSet(publicId, prices);
    }

    private void sendTaskPriceSet (String publicId, PricingService.TaskPrices prices) {
        Map<String, Object> priceEventFieldsMap = new HashMap<>();
        priceEventFieldsMap.put("publicId", publicId);
        priceEventFieldsMap.put("assignePrice", prices.getAssignePrice());
        priceEventFieldsMap.put("closePrice", prices.getClosePrice());
        EventEnvelope event = EventEnvelope.preSetBuilder()
                .eventType(EventType.ATES_TASK_PRICE_SET)
                .eventVersion("1")
                .producer(MqConfig.ATES_BILLING_PRODUCER_NAME)
                .data(priceEventFieldsMap).build();
        applicationEventPublisher.publishEvent(new ProducerNotifyEvent(this, event));
    }

}
