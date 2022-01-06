package ru.zim.ates.billing.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zim.ates.billing.dto.TaskFromEventDto;
import ru.zim.ates.common.application.exception.AppException;
import ru.zim.ates.common.messaging.consumer.AbstractConsumer;
import ru.zim.ates.common.messaging.consumer.PersistEventConsumer;
import ru.zim.ates.common.messaging.schemaregistry.EventEnvelope;
import ru.zim.ates.common.messaging.schemaregistry.EventSchemaRegistry;
import ru.zim.ates.common.messaging.utils.Utils;

@Service
@Slf4j
public class TasksStreamConsumer extends PersistEventConsumer {
    @Autowired
    private EventSchemaRegistry eventSchemaRegistry;
    @Autowired
    private TaskService taskService;

    @SneakyThrows
    @Override
    protected void processMessage(EventEnvelope eventEnvelope) {
        switch (eventEnvelope.getEventType()) {
            case ATES_TASK_CREATED:
            case ATES_TASK_UPDATED:
                onTaskCUD(eventEnvelope);
                break;
            default:
                throw new AppException(String.format("Unexpect event type: %s", eventEnvelope.getEventType()));
        }
    }

    @SneakyThrows
    private void onTaskCUD(EventEnvelope eventEnvelope) {
        assertVersion(eventEnvelope, "1");
        TaskFromEventDto dto = Utils.mapper.readValue(eventEnvelope.getData().toString(), TaskFromEventDto.class);
        taskService.createOrUpdate(dto);
    }
}

