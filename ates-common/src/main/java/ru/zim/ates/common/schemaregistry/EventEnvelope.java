package ru.zim.ates.common.schemaregistry;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zim.ates.common.schemaregistry.utils.RawJsonDeserializer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventEnvelope {

    public static EventEnvelope.EventEnvelopeBuilder preSetBuilder() {
        return EventEnvelope.builder()
                .eventTime(LocalDateTime.now())
                .eventId(java.util.UUID.randomUUID().toString().toUpperCase());
    }

    private String eventId;
    private EventType eventType;
    private String eventVersion;
    private LocalDateTime eventTime;
    private String producer;
    @JsonDeserialize(using = RawJsonDeserializer.class)
    private Object data;
}
