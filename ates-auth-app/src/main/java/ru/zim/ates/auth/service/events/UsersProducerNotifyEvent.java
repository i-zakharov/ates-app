package ru.zim.ates.auth.service.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.zim.ates.common.schemaregistry.EventEnvelope;

@Getter
public class UsersProducerNotifyEvent extends ApplicationEvent {
    private EventEnvelope eventEnvelope;

    public UsersProducerNotifyEvent(Object source, EventEnvelope eventEnvelope) {
        super(source);
        this.eventEnvelope = eventEnvelope;
    }
}
