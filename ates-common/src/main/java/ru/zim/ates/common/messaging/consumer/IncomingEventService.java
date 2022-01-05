package ru.zim.ates.common.messaging.consumer;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zim.ates.common.messaging.schemaregistry.EventEnvelope;
import ru.zim.ates.common.messaging.schemaregistry.EventSchemaRegistry;
import ru.zim.ates.common.messaging.utils.Utils;

@Slf4j
@Service
public class IncomingEventService {

    @Autowired
    EventSchemaRegistry eventSchemaRegistry;
    @Autowired
    IncomingEventRepository incomingEventRepository;

    @Transactional
    public ParseAndSaveResult parseAndSave(String message) {
        ParseAndSaveResult.ParseAndSaveResultBuilder resultBuilder = ParseAndSaveResult.builder().isParseSuccess(false);
        IncomingEvent.IncomingEventBuilder incomingEventBuilder = IncomingEvent.preSetBuilder();
        incomingEventBuilder.rawPayload(message);
        try {
            EventEnvelope eventEnvelope = eventSchemaRegistry.parseAndValidate(message);
            incomingEventBuilder.parsedPublicId(eventEnvelope.getEventId())
                    .parsedGenerationTime(eventEnvelope.getEventTime())
                    .parsedType(eventEnvelope.getEventType().toString())
                    .parsedProducer(eventEnvelope.getProducer())
                    .parsedVersion(eventEnvelope.getEventVersion());

            incomingEventBuilder.status(IncomingEventStatus.NEW);
            resultBuilder.eventEnvelope(eventEnvelope);
            resultBuilder.isParseSuccess(true);
        } catch (Exception e) {
            String errorText = Utils.formatExceptionForLogging(e);
            log.error("Error while parseAndSave: {}", errorText);
            incomingEventBuilder.consumerError(errorText).status(IncomingEventStatus.ERROR);
            resultBuilder.errorText(errorText);
            resultBuilder.isParseSuccess(false);
        }
        resultBuilder.incomingEvent(incomingEventRepository.save(incomingEventBuilder.build()));
        return resultBuilder.build();
    }

    @Transactional
    public IncomingEvent markAsProcessed(Long id) {
        IncomingEvent incomingEvent = incomingEventRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException(String.format("Event with id=%d not found", id)));
        incomingEvent.setProcessingTime(LocalDateTime.now());
        incomingEvent.setStatus(IncomingEventStatus.PROCESSED);
        return incomingEventRepository.save(incomingEvent);
    }

    @Transactional
    public IncomingEvent markAsError(Long id, String errorText) {
        IncomingEvent incomingEvent = incomingEventRepository.findAndLockById(id).orElseThrow(()
                -> new IllegalArgumentException(String.format("Event with id=%d not found", id)));
        incomingEvent.setStatus(IncomingEventStatus.ERROR);
        incomingEvent.setConsumerError(errorText);
        incomingEvent.setRetryCounter(incomingEvent.getRetryCounter() == null ? 1 : incomingEvent.getRetryCounter() + 1);
        return incomingEventRepository.save(incomingEvent);
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class ParseAndSaveResult {
        private final EventEnvelope eventEnvelope;
        private final IncomingEvent incomingEvent;
        private final String errorText;
        private final boolean isParseSuccess;
    }
}
