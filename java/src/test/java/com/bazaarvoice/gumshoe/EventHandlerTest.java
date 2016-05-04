package com.bazaarvoice.gumshoe;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.times;

import java.util.HashMap;
import java.util.Map;

public class EventHandlerTest extends Assert {
    private Filter filter;
    private Decorator decorator;
    private Publisher publisher;
    private EventHandler eventHandler;
    private Map<String, Object> rawEvent;
    private Map<String, Object> decoratedEvent;

    @BeforeMethod
    public void setUp() {
        filter = mock(Filter.class);
        decorator = mock(Decorator.class);
        publisher = mock(Publisher.class);
        eventHandler = new EventHandler(filter, decorator, publisher);
        rawEvent = new HashMap<String, Object>();
        decoratedEvent = new HashMap<String, Object>();
        decoratedEvent.put("extra_data", "foo");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void ensureFilteredEventsAreNotPublished() {
        when(filter.shouldDispatch(rawEvent)).thenReturn(false);

        eventHandler.handle(rawEvent);

        verify(publisher, never()).publish(isA(Map.class));
    }

    @Test
    public void ensureUnFilteredEventsArePublished() {
        when(filter.shouldDispatch(rawEvent)).thenReturn(true);
        when(decorator.decorate(rawEvent)).thenReturn(decoratedEvent);

        eventHandler.handle(rawEvent);

        verify(publisher, times(1)).publish(decoratedEvent);
    }
}
