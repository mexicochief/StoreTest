package org.store.test.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String entityName) {
        super("Entity: " + entityName + " not found");
    }
}
