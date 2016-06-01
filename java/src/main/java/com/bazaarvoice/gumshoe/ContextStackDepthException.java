package com.bazaarvoice.gumshoe;

/**
 * Indicates that a maximum context stack depth has been exceeded
 *
 * @author lance.woodson
 *
 */
public class ContextStackDepthException extends RuntimeException {
    public ContextStackDepthException(int size) {
        super(String.format("Context depth of %s exceeded", size));
    }
}
