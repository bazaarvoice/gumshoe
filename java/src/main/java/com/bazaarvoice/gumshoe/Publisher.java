package com.bazaarvoice.gumshoe;

import java.util.Map;

/**
 * Interface of objects that can publish to some destination
 * for out-of-band consumption.
 *
 * @author lance.woodson
 *
 */
public interface Publisher {
    /**
     * Publish the event
     *
     * @param event
     */
    public void publish(Map<String, Object> event);
}
