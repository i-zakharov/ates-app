package ru.zim.ates.common.messaging.producer;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.zim.ates.common.messaging.schemaregistry.EventEnvelope;

@Getter
public class ProducerNotifyEvent extends ApplicationEvent {
    private EventEnvelope eventEnvelope;

    public ProducerNotifyEvent(Object source, EventEnvelope eventEnvelope) {
        super(source);
        this.eventEnvelope = eventEnvelope;
    }
}
