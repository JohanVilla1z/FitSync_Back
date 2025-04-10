package com.johan.gym_control.exceptions;

/**
 * Exception thrown when a business rule is violated
 */
public class BusinessRuleException extends RuntimeException {

    /**
     * Constructor with message
     *
     * @param message the detail message
     */
    public BusinessRuleException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
