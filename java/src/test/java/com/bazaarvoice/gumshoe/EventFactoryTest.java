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
        event = factory.constructEvent("test event");
    }

    @Test
    public void ensureConstructEventSetsEventType() {
        assertEquals(event.get("type"), "test event");
    }

    @Test
    public void ensureConstructEventIncludesOtherData() {
        assertEquals(event.get("foo"), "bar");
    }
}
