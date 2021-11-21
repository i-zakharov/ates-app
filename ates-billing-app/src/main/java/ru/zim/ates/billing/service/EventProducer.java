package ru.zim.ates.billing.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.zim.ates.common.producer.ProducerNotifyEvent;
import ru.zim.ates.common.schemaregistry.EventSchemaRegistry;
import ru.zim.ates.common.schemaregistry.EventType;
import ru.zim.ates.common.schemaregistry.MqConfig;
import ru.zim.ates.common.schemaregistry.utils.Utils;

@Component
@Slf4j
public class EventProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private EventSchemaRegistry eventSchemaRegistry;

    @SneakyThrows

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void processTransactionalEvent(ProducerNotifyEvent producerNotifyEvent) {
        log.debug("processTransactionalEvent called");
    }

    @SneakyThrows
    @EventListener
    public void processEvent(ProducerNotifyEvent producerNotifyEvent) {
        log.debug("processEvent called");
        String rawMessage = Utils.mapper.writeValueAsString(producerNotifyEvent.getEventEnvelope());
        eventSchemaRegistry.parseAndValidate(rawMessage);
        String exchangeName;
        if (EventType.ATES_TASK_PRICE_SET == producerNotifyEvent.getEventEnvelope().getEventType()) {
            exchangeName = MqConfig.ATES_TASKS_PRICES_EXCHANGE;
        } else {
            throw new NotImplementedException("TODO");
        }
        rabbitTemplate.convertAndSend(exchangeName,
                "", rawMessage);
    }


}
