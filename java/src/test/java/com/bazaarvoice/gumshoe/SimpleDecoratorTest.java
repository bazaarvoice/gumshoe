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
        decorator = new SimpleDecorator();
        event = decorator.decorate(rawEvent);
    }

    @Test
    public void ensureEventsDecoratedWithHostname() throws Exception {
        assertNotNull(event.get("_hostname"));
        assertEquals(event.get("_hostname"), InetAddress.getLocalHost().getHostName());
    }

    @Test
    public void ensureEventsDecoratedWithProcessUser() throws Exception {
        assertNotNull(event.get("_user"));
        assertEquals(event.get("_user"), System.getenv("USER"));
    }

    @Test
    public void ensureEventsDecoratedWithPid() throws Exception {
        assertNotNull(event.get("_pid"));
        assertNotEquals(event.get("_pid"), "UNKNOWN");
    }

    @Test
    public void ensureEventsDecoratedWithThread() {
        assertEquals(event.get("_thread"), Thread.currentThread().getName());
    }
}