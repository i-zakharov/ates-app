package ru.zim.ates.tasktracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.zim.ates.common.messaging.producer.ProducerNotifyEvent;
import ru.zim.ates.common.messaging.schemaregistry.EventCategory;
import ru.zim.ates.common.messaging.schemaregistry.EventSchemaRegistry;
import ru.zim.ates.common.messaging.utils.Utils;
import ru.zim.ates.common.standartimpl.consumer.user.model.AppUser;
import ru.zim.ates.tasktracker.mapper.AppUserSerializer;

import static ru.zim.ates.common.messaging.config.MqConfig.ATES_TASKS_EXCHANGE;
import static ru.zim.ates.common.messaging.config.MqConfig.ATES_TASKS_STREAM_EXCHANGE;

@Component
public class EventProducer {

    private static ObjectMapper mapper;
    static {
        mapper = Utils.mapper.copy();
        SimpleModule module = new SimpleModule();
        module.addSerializer(AppUser.class, new AppUserSerializer());
        mapper.registerModule(module);
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private EventSchemaRegistry eventSchemaRegistry;

    @SneakyThrows
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void processEvent(ProducerNotifyEvent producerNotifyEvent) {
        String rawMessage = mapper.writeValueAsString(producerNotifyEvent.getEventEnvelope());
        eventSchemaRegistry.parseAndValidate(rawMessage);
        String exchangeName = producerNotifyEvent.getEventEnvelope().getEventType().getEventCategory() == EventCategory.BUSINESS ?
                ATES_TASKS_EXCHANGE : ATES_TASKS_STREAM_EXCHANGE;

        rabbitTemplate.convertAndSend(exchangeName,
                "", rawMessage);
    }

}
