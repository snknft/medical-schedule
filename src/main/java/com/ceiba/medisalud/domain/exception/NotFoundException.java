package com.ceiba.medisalud.domain.exception;

/**
 * Represents a domain exception mapped to HTTP 404 Not Found.
 */
public class NotFoundException extends DomainException {

    /**
     * Creates a not-found exception with the missing resource message.
     *
     * @param message detail message returned to API clients
     */    public NotFoundException(String message) {
        super(message);
    }
}
