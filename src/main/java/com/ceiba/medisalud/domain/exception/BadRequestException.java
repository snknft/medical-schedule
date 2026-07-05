package com.ceiba.medisalud.domain.exception;

/**
 * Represents a domain exception mapped to HTTP 400 Bad Request.
 */
public class BadRequestException extends DomainException {

    /**
     * Creates a bad request exception with a domain validation message.
     *
     * @param message detail message returned to API clients
     */    public BadRequestException(String message) {
        super(message);
    }
}
