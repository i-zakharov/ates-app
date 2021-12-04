package ru.zim.ates.common.consumer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zim.ates.common.model.IncomingEvent;
import ru.zim.ates.common.model.IncomingEventStatus;
import ru.zim.ates.common.repository.IncomingEventRepository;
import ru.zim.ates.common.schemaregistry.EventEnvelope;
import ru.zim.ates.common.schemaregistry.EventSchemaRegistry;

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
            resultBuilder.isParseSuccess(true);
        } catch (Throwable e) {
            incomingEventBuilder.consumerError(e.getMessage()).status(IncomingEventStatus.ERROR);
            resultBuilder.isParseSuccess(false);
        }
        resultBuilder.incomingEvent(incomingEventRepository.save(incomingEventBuilder.build()));
        return resultBuilder.build();
    }


    @Builder
    @Getter
    @AllArgsConstructor
    public static class ParseAndSaveResult {
        private final EventEnvelope eventEnvelope;
        private final IncomingEvent incomingEvent;
        private final boolean isParseSuccess;
    }
}
