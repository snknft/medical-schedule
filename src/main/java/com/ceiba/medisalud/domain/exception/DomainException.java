package com.ceiba.medisalud.domain.exception;

/**
 * Base type for domain exceptions translated by the REST exception handler.
 */
public abstract class DomainException extends RuntimeException {

    /**
     * Creates a domain exception with a human-readable message.
     *
     * @param message detail message returned to API clients
     */    protected DomainException(String message) {
        super(message);
    }
}
