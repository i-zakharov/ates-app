package ru.zim.ates.tasktracker.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.zim.ates.common.producer.ProducerNotifyEvent;
import ru.zim.ates.common.schemaregistry.EventEnvelope;
import ru.zim.ates.common.schemaregistry.EventSchemaRegistry;
import ru.zim.ates.common.schemaregistry.EventType;
import ru.zim.ates.common.schemaregistry.MqConfig;
import ru.zim.ates.common.schemaregistry.utils.Utils;

@Component
public class BillingStubEventProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private EventSchemaRegistry eventSchemaRegistry;

    @SneakyThrows
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void processEvent(ProducerNotifyEvent producerNotifyEvent) {

        if (EventType.ATES_TASK_PENDING.equals(producerNotifyEvent.getEventEnvelope().getEventType())) {
            Map<String, Object> eventFieldsMap = (Map<String, Object>) producerNotifyEvent.getEventEnvelope().getData();
            String publicId = eventFieldsMap.get("publicId").toString();

            Map<String, Object> priceEventFieldsMap = new HashMap<>();
            priceEventFieldsMap.put("publicId", publicId);
            priceEventFieldsMap.put("price", BigDecimal.valueOf(99.99));
            EventEnvelope priceEvent = EventEnvelope.preSetBuilder()
                .eventType(EventType.ATES_TASK_PRICE_SET)
                .eventVersion("1")
                .producer(MqConfig.ATES_TASK_TRACKER_PRODUCER_NAME)
                .data(priceEventFieldsMap).build();
            String rawMessage = Utils.mapper.writeValueAsString(priceEvent);
            eventSchemaRegistry.parseAndValidate(rawMessage);

            rabbitTemplate.convertAndSend(MqConfig.ATES_TASKS_PRICES_EXCHANGE,
                    "", rawMessage);
        }
    }
}
