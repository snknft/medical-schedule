package com.ceiba.medisalud.domain.exception;

/**
 * Represents a domain exception mapped to HTTP 409 Conflict.
 */
public class ConflictException extends DomainException {

    /**
     * Creates a conflict exception with a business conflict message.
     *
     * @param message detail message returned to API clients
     */    public ConflictException(String message) {
        super(message);
    }
}
