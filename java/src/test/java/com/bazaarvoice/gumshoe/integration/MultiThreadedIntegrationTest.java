package com.bazaarvoice.gumshoe.integration;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.bazaarvoice.gumshoe.Configuration;
import com.bazaarvoice.gumshoe.Decorator;
import com.bazaarvoice.gumshoe.Filter;
import com.bazaarvoice.gumshoe.Gumshoe;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.anyMap;

public class MultiThreadedIntegrationTest extends Assert {
    private Filter filter;
    private Decorator decorator;
    private InMemoryPublisher publisher;
    private Configuration configuration;

    @BeforeMethod
    public void setUp() {
        Gumshoe.clear();
        filter = mock(Filter.class);
        decorator = new TestDecorator();
        publisher = new InMemoryPublisher();

        configuration = new Configuration();
        configuration.setFilter(filter);
        configuration.setDecorator(decorator);
        configuration.setPublisher(publisher);

        Gumshoe.configure(configuration);

        when(filter.shouldDispatch(anyMap())).thenReturn(true);
    }

    @Test
    public void test() throws Exception {
        Counter counter1 = new Counter("counter1", 5);
        Counter counter2 = new Counter("counter2", 5);

        counter1.start();
        counter2.start();

        Thread.sleep(200);

        List<Map<String, Object>> events = publisher.getEvents();
        assertEquals(events.size(), 14);

        List<Map<String, Object>> counter1Events = new ArrayList<Map<String,Object>>();
        List<Map<String, Object>> counter2Events = new ArrayList<Map<String,Object>>();

        for(Map<String, Object> event : events) {
            if (event.get("name").equals("counter1")) {
                counter1Events.add(event);
            } else if (event.get("name").equals("counter2")) {
                counter2Events.add(event);
            }
        }

        assertEquals(counter1Events.size(), 7);
        String stream1Id = (String)counter1Events.get(0).get("stream_id");
        assertNotNull(stream1Id);
        for (Map<String, Object> event : counter1Events) {
            assertEquals(event.get("stream_id"), stream1Id);
        }

        assertEquals(counter2Events.size(), 7);
        String stream2Id = (String)counter2Events.get(0).get("stream_id");
        assertNotNull(stream2Id);
        assertNotEquals(stream1Id, stream2Id);
        for (Map<String, Object> event : counter2Events) {
            assertEquals(event.get("stream_id"), stream2Id);
        }
    }

    private static class Counter extends Thread {
        private String name;
        private int number;

        public Counter(String name, int number) {
            this.name = name;
            this.number = number;
        }

        @Override
        public void run() {
            Gumshoe gumshoe = Gumshoe.get();
            gumshoe.context("counter").put("name", name).start();
            for (int i = 0; i < number; i++) {
                gumshoe.context().put("i", i);
                gumshoe.emit("iterating");
            }
            gumshoe.finish();
        }
    }
}
