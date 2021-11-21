package ru.zim.ates.common.producer;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.zim.ates.common.schemaregistry.EventEnvelope;

@Getter
public class ProducerNotifyEvent extends ApplicationEvent {
    private EventEnvelope eventEnvelope;

    public ProducerNotifyEvent(Object source, EventEnvelope eventEnvelope) {
        super(source);
        this.eventEnvelope = eventEnvelope;
    }
}
