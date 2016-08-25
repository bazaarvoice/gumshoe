package com.bazaarvoice.gumshoe;

/**
 * Interface of objects that can be invoked within a context
 *
 * @author lance.woodson
 *
 */
public interface VoidCallable {
    void call() throws Exception;
}
