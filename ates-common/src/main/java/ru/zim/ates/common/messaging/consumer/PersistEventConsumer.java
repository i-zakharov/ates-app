package ru.zim.ates.common.messaging.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.zim.ates.common.messaging.schemaregistry.EventEnvelope;
import ru.zim.ates.common.messaging.utils.Utils;

@Slf4j
public abstract class PersistEventConsumer implements AbstractConsumer {

    private static int MAX_RETRY_COUNT = 3;

    @Autowired
    IncomingEventService incomingEventService;

    @Override
    public void receiveMessage(String message) {
        log.info("Received: {}", message);
        IncomingEventService.ParseAndSaveResult parseAndSaveResult = incomingEventService.parseAndSave(message);
        if (!parseAndSaveResult.isParseSuccess()) {
            logParceError(message, parseAndSaveResult.getErrorText());
            return;
        }
        for (int i = 1; i <= MAX_RETRY_COUNT; i++) {
            try {
                processMessage(parseAndSaveResult.getEventEnvelope());
                incomingEventService.markAsProcessed(parseAndSaveResult.getIncomingEvent().getId());
                log.info("Processed: {}", message);
                break;
            } catch (Throwable e) {
                incomingEventService.markAsError(parseAndSaveResult.getIncomingEvent().getId(),
                        Utils.formatExceptionForLogging(e));
                logProcessingError(message, e);
            }
        }
    }

    private void logParceError(String message, String error) {
        log.error(new StringBuilder()
                .append("Failed to parse message:")
                .append(System.lineSeparator())
                .append(message)
                .append(System.lineSeparator())
                .append("Error:")
                .append(error).toString());
    }

    private void logProcessingError(String message, Throwable error) {
        log.error(new StringBuilder()
                .append("Failed to process message:")
                .append(System.lineSeparator())
                .append(message)
                .append(System.lineSeparator())
                .append("Error:")
                .append(Utils.formatExceptionForLogging(error)).toString());
    }

    protected abstract void processMessage(EventEnvelope eventEnvelope);

}
