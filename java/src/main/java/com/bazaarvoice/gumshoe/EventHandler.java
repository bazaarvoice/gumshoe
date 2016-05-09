package com.bazaarvoice.gumshoe;

import java.util.Map;

/**
 * Processes and dispatches events for Gumshoe by:
 *
 * - seeing if the event should be published
 * - decorating an event with global data
 * - publishing the event in some way
 *
 * @author lance.woodson
 *
 */
public class EventHandler {
    private Filter filter;
    private Decorator decorator;
    private Publisher publisher;

    public EventHandler(Configuration configuration) {
        this(configuration.getFilter(), configuration.getDecorator(), configuration.getPublisher());
    }

    public EventHandler(Filter filter, Decorator decorator, Publisher publisher) {
        this.filter = filter;
        this.decorator = decorator;
        this.publisher = publisher;
    }

    /**
     * Handle the event, first seeing if it should be dispatched according to
     * the configured Filter, then decorating it with the configured Decorator
     * and finally publishing it with the configured Publisher.
     *
     * @param event
     */
    public void handle(Map<String, Object> event) {
        if (filter.shouldDispatch(event)) {
            publisher.publish(decorator.decorate(event));
        }
    }
}
