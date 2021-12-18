package ru.zim.ates.common.messaging.schemaregistry.exception;

public abstract class SchemaRegistryException extends Exception {

    public SchemaRegistryException(String message) {
        super(message);
    }

    public SchemaRegistryException(String message, Throwable cause) {
        super(message, cause);
    }

    public SchemaRegistryException(Throwable cause) {
        super(cause);
    }
}
