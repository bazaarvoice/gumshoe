package com.bazaarvoice.gumshoe.integration;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.bazaarvoice.gumshoe.SimpleConfiguration;
import com.bazaarvoice.gumshoe.Attribute;
import com.bazaarvoice.gumshoe.Decorator;
import com.bazaarvoice.gumshoe.Filter;
import com.bazaarvoice.gumshoe.Gumshoe;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.anyMap;

public class SingleThreadedIntegrationTest extends Assert {
    private Filter filter;
    private Decorator decorator;
    private InMemoryPublisher publisher;
    private SimpleConfiguration configuration;
    private Gumshoe gumShoe;

    @BeforeMethod
    public void setUp() {
        Gumshoe.clear();
        filter = mock(Filter.class);
        decorator = new TestDecorator();
        publisher = new InMemoryPublisher();

        configuration = new SimpleConfiguration("SingleThreadedIntegrationTest");
        configuration.setFilter(filter);
        configuration.setDecorator(decorator);
        configuration.setPublisher(publisher);

        Gumshoe.configure(configuration);
        gumShoe = Gumshoe.get();

        when(filter.shouldDispatch(anyMap())).thenReturn(true);
    }

    @Test
    public void testSingleContextSimpleCase() throws Exception {
        gumShoe.context("single_context_test").start();
        Thread.sleep(10);
        gumShoe.context().finish();

        assertEquals(publisher.getEvents().size(), 2);
        assertEvent(publisher.getEvents().get(0), Attribute.asPath("single_context_test"), "started");
        assertEvent(publisher.getEvents().get(1), Attribute.asPath("single_context_test"), "finished");
    }

    @Test
    public void testSingleFailedContext() throws Exception {
        gumShoe.context("single_failed_context").start();
        Thread.sleep(10);
        gumShoe.context().fail();

        assertEquals(publisher.getEvents().size(), 2);
        assertEvent(publisher.getEvents().get(0), Attribute.asPath("single_failed_context"), "started");
        assertEvent(publisher.getEvents().get(1), Attribute.asPath("single_failed_context"), "failed");
    }

    @Test
    public void testSingleContextWithData() throws Exception {
        gumShoe.context("single_context_with_data_test")
            .put("foo", "bar")
            .put("id", 1)
            .start();
        Thread.sleep(10);
        gumShoe.context().finish();

        assertEquals(publisher.getEvents().size(), 2);

        assertEvent(publisher.getEvents().get(0), Attribute.asPath("single_context_with_data_test"), "started");
        assertEquals(publisher.getEvents().get(0).get("foo"), "bar");
        assertEquals(publisher.getEvents().get(0).get("id"), 1);

        assertEvent(publisher.getEvents().get(1), Attribute.asPath("single_context_with_data_test"), "finished");
        assertEquals(publisher.getEvents().get(1).get("foo"), "bar");
        assertEquals(publisher.getEvents().get(1).get("id"), 1);
    }

    @Test
    public void testNestedCase() throws Exception {
        gumShoe.context("nested_contexts_test").put("orientation", "top").start();
        gumShoe.emit("at_top");
        Thread.sleep(10);
        gumShoe.context("nested").put("orientation", "bottom").start();
        gumShoe.emit("at_bottom");
        Thread.sleep(10);
        gumShoe.finish();

        assertEquals(publisher.getEvents().size(), 6);

        assertEvent(publisher.getEvents().get(0), Attribute.asPath("nested_contexts_test"), "started");
        assertEquals(publisher.getEvents().get(0).get("orientation"), "top");

        assertEvent(publisher.getEvents().get(1), Attribute.asPath("nested_contexts_test"), "at_top");
        assertEquals(publisher.getEvents().get(1).get("orientation"), "top");

        assertEvent(publisher.getEvents().get(2), Attribute.asPath("nested_contexts_test", "nested"), "started");
        assertEquals(publisher.getEvents().get(2).get("orientation"), "bottom");

        assertEvent(publisher.getEvents().get(3), Attribute.asPath("nested_contexts_test", "nested"), "at_bottom");
        assertEquals(publisher.getEvents().get(3).get("orientation"), "bottom");

        assertEvent(publisher.getEvents().get(4), Attribute.asPath("nested_contexts_test", "nested"), "finished");
        assertEquals(publisher.getEvents().get(4).get("orientation"), "bottom");

        assertEvent(publisher.getEvents().get(5), Attribute.asPath("nested_contexts_test"), "finished");
        assertEquals(publisher.getEvents().get(5).get("orientation"), "top");
    }

    private void assertEvent(Map<String, Object> event, String context, String type) {
        assertEquals(event.get(Attribute.named("context")), context);
        assertEquals(event.get(Attribute.named("event_type")), type);
        assertEquals(event.get(Attribute.named("test")), Boolean.TRUE);
        if (type.equals("finished")) {
            assertTrue((Long)event.get(Attribute.named("elapsed")) > 0);
        }
    }
}
