package com.bazaarvoice.gumshoe;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SimpleDecoratorTest extends Assert {
    private Map<String, Object> rawEvent;
    private SimpleDecorator decorator;
    private Map<String, Object> event;

    @BeforeMethod
    public void setUp() {
        rawEvent = new HashMap<String, Object>();
        decorator = new SimpleDecorator("test-suite");
        event = decorator.decorate(rawEvent);
    }

    @Test
    public void ensureEventsDecoratedWithAppName() {
        assertEquals(event.get(Attribute.named("application")), "test-suite");
    }

    @Test
    public void ensureEventsDecoratedWithHostname() throws Exception {
        assertNotNull(event.get(Attribute.named("hostname")));
        assertEquals(event.get(Attribute.named("hostname")), InetAddress.getLocalHost().getHostName());
    }

    @Test
    public void ensureEventsDecoratedWithProcessUser() throws Exception {
        assertNotNull(event.get(Attribute.named("user")));
        assertEquals(event.get(Attribute.named("user")), System.getenv("USER"));
    }

    @Test
    public void ensureEventsDecoratedWithPid() throws Exception {
        assertNotNull(event.get(Attribute.named("pid")));
        assertNotEquals(event.get(Attribute.named("pid")), "UNKNOWN");
    }

    @Test
    public void ensureEventsDecoratedWithThread() {
        assertEquals(event.get(Attribute.named("thread")), Thread.currentThread().getName());
    }

    @Test
    public void ensureEventsDecoratedWithEmittedAtTimestamp() {
        assertTrue(((String)event.get(Attribute.named("emitted_at"))).matches("\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d,\\d\\d\\d"));
    }
}
