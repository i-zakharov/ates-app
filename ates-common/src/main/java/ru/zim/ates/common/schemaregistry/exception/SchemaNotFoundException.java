package ru.zim.ates.common.schemaregistry.exception;

import ru.zim.ates.common.schemaregistry.EventType;

public class SchemaNotFoundException extends SchemaRegistryException {
    public SchemaNotFoundException(EventType eventType) {
        super(String.format("There is no schemes for event %s", eventType.name()));
    }
}
