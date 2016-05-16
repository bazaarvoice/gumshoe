package com.bazaarvoice.gumshoe;

import java.util.HashMap;
import java.util.Map;

/**
 * Objects that maintain a stack of data and can construct events of
 * various types using the data stored in the stack.
 *
 * @author lance.woodson
 *
 */
public  class EventFactory {
    private StackedMap<String, Object> dataStack;

    public EventFactory() {
        dataStack = new StackedMap<String, Object>();
    }

    /**
     * Access the data stack
     *
     * @return
     */
    public StackedMap<String, Object> getDataStack() {
        return dataStack;
    }

    /**
     * Construct an event of a type including all the data in the
     * stack
     *
     * @param type
     * @return
     */
    public Map<String, Object> constructEvent(String type) {
        Map<String, Object> event = new HashMap<String, Object>(getDataStack().flatten());
        event.put(Attribute.named("event_type"), type);
        return event;
    }
}
