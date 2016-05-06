package com.bazaarvoice.gumshoe;

import java.util.Map;

/**
 * Interface of objects that can be used to filter or throttle
 * events that are dispatched by Gumshoe.
 *
 * @author lance.woodson
 *
 */
public interface Filter {
    /**
     * Examines the event and returns true if it should be
     * dispatched or false otherwise.
     *
     * @param event
     * @return
     */
    public boolean shouldDispatch(Map<String, Object> event);
}
