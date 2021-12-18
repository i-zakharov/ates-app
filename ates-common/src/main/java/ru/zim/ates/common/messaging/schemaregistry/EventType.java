package ru.zim.ates.common.messaging.schemaregistry;

public interface EventType {
    String getExchangeName();

    EventCategory getEventCategory();

    String name();
}
