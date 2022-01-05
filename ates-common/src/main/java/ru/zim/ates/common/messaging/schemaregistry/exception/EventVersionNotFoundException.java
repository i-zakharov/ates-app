package ru.zim.ates.common.messaging.schemaregistry.exception;

import ru.zim.ates.common.messaging.schemaregistry.EventType;

public class EventVersionNotFoundException extends SchemaRegistryException {
    public EventVersionNotFoundException (EventType eventType, String version) {
        super(String.format("There is no version %s for event %s", version, eventType.name()));
    }
}
