package ru.zim.ates.common.messaging.config;

import org.springframework.stereotype.Component;
import ru.zim.ates.common.messaging.schemaregistry.EventType;
import ru.zim.ates.common.messaging.schemaregistry.EventTypeProvider;

@Component
public class AppEventTypeProvider implements EventTypeProvider {

    @Override
    public EventType[] getEventTypes() {
        return AppEventType.values();
    }
}
