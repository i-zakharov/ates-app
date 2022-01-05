package ru.zim.ates.common.messaging.schemaregistry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import ru.zim.ates.common.messaging.schemaregistry.exception.EventValidationException;
import ru.zim.ates.common.messaging.schemaregistry.exception.EventVersionNotFoundException;
import ru.zim.ates.common.messaging.schemaregistry.exception.SchemaNotFoundException;
import ru.zim.ates.common.messaging.schemaregistry.exception.SchemaRegistryException;

import static ru.zim.ates.common.messaging.utils.Utils.mapper;

@Service
public class EventSchemaRegistry {

    private Map<EventType, Map<String, JsonSchema>> registry = new HashMap<>();
    private EventTypeProvider eventTypeProvider;

    @Autowired
    public EventSchemaRegistry(EventTypeProvider eventTypeProvider) {
        this.eventTypeProvider = eventTypeProvider;
    }

    @PostConstruct
    public void init() throws IOException {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        for (EventType ei : eventTypeProvider.getEventTypes()) {
            String pattern = "schemaregistry/shemas/" + ei.getExchangeName() + "/" + ei.name() + "/*.json";
            Resource[] foundResources = resourcePatternResolver.getResources(pattern);
            Map<String, JsonSchema> versionMap = new HashMap<>();
            for (Resource ri : foundResources) {
                JsonSchema jsonSchema = loadSchema(factory, ri);
                versionMap.put(FilenameUtils.removeExtension(ri.getFilename()), jsonSchema);
            }
            registry.put(ei, versionMap);
        }
    }

    public EventEnvelope parseAndValidate(String message) throws JsonProcessingException, SchemaRegistryException {
        EventEnvelope eventEnvelope = mapper.readValue(message, EventEnvelope.class);
        validate(message, eventEnvelope.getEventType(), eventEnvelope.getEventVersion());
        return eventEnvelope;
    }

    public void validate(JsonNode jsonNode, EventType eventType, String version) throws SchemaRegistryException {
        JsonSchema jsonSchema = getJsonSchema(eventType, version);
        Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
        if (errors.size() > 0) {
            throw new EventValidationException(errors);
        }
    }

    public void validate(String message, EventType eventType, String version) throws JsonProcessingException, SchemaRegistryException {
        validate(mapper.readTree(message), eventType, version);
    }


    JsonSchema getJsonSchema(EventType eventType, String version) throws SchemaRegistryException {
        Map<String, JsonSchema> versionMap = registry.get(eventType);
        if (versionMap == null) {
            throw new SchemaNotFoundException(eventType);
        }
        JsonSchema jsonSchema = versionMap.get(version);
        if (jsonSchema == null) {
            throw new EventVersionNotFoundException(eventType, version);
        }
        return jsonSchema;
    }

    private JsonSchema loadSchema(JsonSchemaFactory factory, Resource resource) throws IOException {
        InputStream is = resource.getInputStream();
        JsonSchema jsonSchema = factory.getSchema(is);
        jsonSchema.initializeValidators();
        return jsonSchema;

    }

}
