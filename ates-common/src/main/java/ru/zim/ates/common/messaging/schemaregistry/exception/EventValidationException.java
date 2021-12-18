package ru.zim.ates.common.messaging.schemaregistry.exception;

import com.networknt.schema.ValidationMessage;
import java.util.Set;

public class EventValidationException extends SchemaRegistryException{
    private static String buildMessage(Set<ValidationMessage> errors) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.append("There are ")
                    .append(errors.size())
                    .append(" errors:");
            errors.stream().forEach(i -> stringBuilder.append("\n").append(i.getMessage()));
        } catch (RuntimeException e) {
            return e.getMessage();
        }
        return stringBuilder.toString();
    }

    public EventValidationException(Set<ValidationMessage> errors) {
        super(buildMessage(errors));
    }

}
