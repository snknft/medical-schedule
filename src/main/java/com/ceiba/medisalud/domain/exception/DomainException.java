package com.ceiba.medisalud.domain.exception;

/**
 * Represents a domain domain exception.
 */
public abstract class DomainException extends RuntimeException {

    /**
     * Creates a new DomainException instance.
     */
    protected DomainException(String message) {
        super(message);
    }
}
