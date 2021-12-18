package ru.zim.ates.common.messaging.consumer;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "INCOMING_EVENT")
public class IncomingEvent {
    public static IncomingEvent.IncomingEventBuilder preSetBuilder() {
        return IncomingEvent.builder().status(IncomingEventStatus.NEW).reciveTime(LocalDateTime.now()).retryCounter(0);
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "PARSED_PUBLIC_ID")
    private String parsedPublicId;
    @Column(name = "RECIVE_TIME")
    private LocalDateTime reciveTime;
    @Column(name = "PROCESSING_TIME")
    private LocalDateTime processingTime;
    @Column(name = "RAW_PAYLOAD")
    private String rawPayload;
    @Column(name = "CONSUMER_ERROR")
    private String consumerError;
    @Column(name = "RETRY_COUNTER")
    private Integer retryCounter;
    @Column(name = "STATUS")
    private IncomingEventStatus status;
    @Column(name = "PARSED_TYPE")
    private String parsedType;
    @Column(name = "PARSED_PRODUCER")
    private String parsedProducer;
    @Column(name = "PARSED_GENERATION_TIME")
    private LocalDateTime parsedGenerationTime;
    @Column(name = "PARSED_VERSION")
    private String parsedVersion;
}
