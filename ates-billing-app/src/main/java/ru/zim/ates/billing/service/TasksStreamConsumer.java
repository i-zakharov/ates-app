package ru.zim.ates.billing.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.zim.ates.billing.dto.TaskFromEventDto;
import ru.zim.ates.common.consumer.BaseConsumer;
import ru.zim.ates.common.exception.AppException;
import ru.zim.ates.common.schemaregistry.EventEnvelope;
import ru.zim.ates.common.schemaregistry.EventSchemaRegistry;
import ru.zim.ates.common.schemaregistry.utils.Utils;
import ru.zim.ates.common.standartimpl.consumer.user.dto.AppUserFromEventDto;

@Service
@Slf4j
public class TasksStreamConsumer extends BaseConsumer {
    @Autowired
    private EventSchemaRegistry eventSchemaRegistry;
    @Autowired
    private TaskService taskService;

    @Autowired
    public TasksStreamConsumer(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @SneakyThrows
    @Override
    protected void processMessage(String message) {
        EventEnvelope eventEnvelope = eventSchemaRegistry.parseAndValidate(message);
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
