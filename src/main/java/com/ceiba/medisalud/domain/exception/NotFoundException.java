package com.ceiba.medisalud.domain.exception;

/**
 * Represents a notfound domain exception.
 */
public class NotFoundException extends DomainException {

    /**
     * Creates a new NotFoundException instance.
     */
    public NotFoundException(String message) {
        super(message);
    }
}
