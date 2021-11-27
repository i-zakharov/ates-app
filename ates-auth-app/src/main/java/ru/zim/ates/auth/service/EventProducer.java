package ru.zim.ates.auth.service;

import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.zim.ates.common.producer.ProducerNotifyEvent;
import ru.zim.ates.common.schemaregistry.EventCategory;
import ru.zim.ates.common.schemaregistry.EventSchemaRegistry;
import ru.zim.ates.common.schemaregistry.utils.Utils;

import static ru.zim.ates.common.schemaregistry.MqConfig.ATES_USERS_EXCHANGE;
import static ru.zim.ates.common.schemaregistry.MqConfig.ATES_USERS_STREAM_EXCHANGE;

@Component
public class EventProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private EventSchemaRegistry eventSchemaRegistry;

    @SneakyThrows
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void processEvent(ProducerNotifyEvent producerNotifyEvent) {

        String rawMessage = Utils.mapper.writeValueAsString(producerNotifyEvent.getEventEnvelope());
        eventSchemaRegistry.parseAndValidate(rawMessage);
        String exchangeName = producerNotifyEvent.getEventEnvelope().getEventType().getEventCategory() == EventCategory.BUSINESS ?
                ATES_USERS_EXCHANGE : ATES_USERS_STREAM_EXCHANGE;

        rabbitTemplate.convertAndSend(exchangeName,
                "", rawMessage);
    }

}
