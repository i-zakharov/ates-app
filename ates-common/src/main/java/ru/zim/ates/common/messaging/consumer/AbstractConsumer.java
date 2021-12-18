package ru.zim.ates.common.messaging.consumer;

import ru.zim.ates.common.application.exception.AppException;
import ru.zim.ates.common.messaging.schemaregistry.EventEnvelope;

public interface AbstractConsumer {

    static void assertVersion(EventEnvelope eventEnvelope, String version) {
        if (!version.equals(eventEnvelope.getEventVersion())) {
            throw new AppException(String.format("Not supported event version: %s for event %s", eventEnvelope.getEventVersion(),
                    eventEnvelope.getEventType()));
        }
    }

    void receiveMessage(String message);
}
