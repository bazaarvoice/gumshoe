package com.bazaarvoice.gumshoe;

/**
 * Configuration POJO to allow specification of following extension points:
 *
 * - Filter used to filter out events emitted
 * - Decorator used to decorate all events with global data
 * - Publisher used to publish events in some fashion.
 *
 * @author lance.woodson
 *
 */
public class SimpleConfiguration implements Configuration {
    private String applicationName;
    private Filter filter;
    private Decorator decorator;
    private Publisher publisher;

    public SimpleConfiguration(String applicationName) {
        this.applicationName = applicationName;
        // TODO wire up default implementations of Filter, Decorator
        // and Publisher
    }
    
    @Override
    public String getApplicationName() {
        return applicationName;
    }
    
    @Override
    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    @Override
    public Decorator getDecorator() {
        return decorator;
    }

    public void setDecorator(Decorator decorator) {
        this.decorator = decorator;
    }

    @Override
    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
}
