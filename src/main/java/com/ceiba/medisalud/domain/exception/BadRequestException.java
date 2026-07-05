package com.ceiba.medisalud.domain.exception;

/**
 * Represents a badrequest domain exception.
 */
public class BadRequestException extends DomainException {

    /**
     * Creates a new BadRequestException instance.
     */
    public BadRequestException(String message) {
        super(message);
    }
}
