package com.bazaarvoice.gumshoe;

import java.util.Map;

/**
 * Interface of objects that can serve as decorators within
 * Gumshoe.  These take an event map and return a decorated
 * result.  Can be used to globally inject all events with
 * some data you want to keep track of, like the hostname
 * where the application is running.
 *
 * @author lance.woodson
 *
 */
public interface Decorator {
    /**
     * Decorate the passed event Map, returning the decorated
     * event
     *
     * @param event
     * @return
     */
    public Map<String, Object> decorate(Map<String, Object> event);
}
