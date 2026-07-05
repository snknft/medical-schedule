package com.ceiba.medisalud.domain.exception;

/**
 * Represents a conflict domain exception.
 */
public class ConflictException extends DomainException {

    /**
     * Creates a new ConflictException instance.
     */
    public ConflictException(String message) {
        super(message);
    }
}
