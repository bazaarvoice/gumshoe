package com.bazaarvoice.gumshoe;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class EventFactoryTest extends Assert {
    private EventFactory factory;
    private Map<String, Object> data;
    private Map<String, Object> event;

    @BeforeMethod
    public void setUp() {
        factory = new EventFactory();
        data = new HashMap<String, Object>();
        data.put("foo", "bar");
        factory.getDataStack().push(data);
    }

    @Test
    public void ensureConstructEventSetsEventType() {
        event = factory.constructEvent("test event");

        assertEquals(event.get(Attribute.named("event_type")), "test event");
    }

    @Test
    public void ensureConstructEventIncludesStackData() {
        event = factory.constructEvent("test event");

        assertEquals(event.get("foo"), "bar");
    }

    @Test
    public void ensureConstructedEventIncludesOtherData() {
        Map<String, Object> otherData = new HashMap<String, Object>();
        otherData.put("bar", "baz");
        event = factory.constructEvent("test event", otherData);

        assertEquals(event.get("bar"), "baz");
    }
}
