package com.bazaarvoice.gumshoe;

/**
 * Interface of objects that can be used to configure Gumshoe.
 * 
 * @author lance.woodson
 *
 */
public interface Configuration {
    /**
     * The name of the application
     * 
     * @return
     */
    String getApplicationName();
    /**
     * Object that decides whether an event will be published or not
     * 
     * @return
     */
    Filter getFilter();
    /**
     * Object that decorates all events with global information
     * 
     * @return
     */
    Decorator getDecorator();
    /**
     * Object that publishes the event in some way
     * @return
     */
    Publisher getPublisher();
}
