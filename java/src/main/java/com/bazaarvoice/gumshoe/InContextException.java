package com.bazaarvoice.gumshoe;

/**
 * Exception raised when an exception happens within a context.
 *
 * @author lance.woodson
 *
 */
public class InContextException extends RuntimeException {
    public InContextException(Exception cause) {
        super(cause);
    }
}
