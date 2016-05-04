package com.bazaarvoice.gumshoe;

/**
 * Configuration to control behavior of GumShoe.  Allows specification of:
 *
 * - Filter used to filter out events emitted
 * - Decorator used to decorate all events with global data
 * - Publisher used to publish events in some fashion.
 *
 * @author lance.woodson
 *
 */
public class Configuration {
    private Filter filter;
    private Decorator decorator;
    private Publisher publisher;

    public Configuration() {
        // TODO wire up default implementations of Filter, Decorator
        // and Publisher
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public Decorator getDecorator() {
        return decorator;
    }

    public void setDecorator(Decorator decorator) {
        this.decorator = decorator;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
}
