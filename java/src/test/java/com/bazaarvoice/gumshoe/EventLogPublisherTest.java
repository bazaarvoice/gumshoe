package com.bazaarvoice.gumshoe;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class EventLogPublisherTest extends Assert {
    private String path;
    private Publisher publisher;
    private Map<String, Object> event;

    @BeforeMethod
    public void setUp() {
        path = "test.log";
        publisher = new EventLogPublisher(path);
        event = new HashMap<String, Object>();
        event.put("a", 1);
        event.put("b", 2);
    }

    @AfterMethod
    public void tearDown() {
        File logFile = new File(path);
        if (logFile.exists()) {
            logFile.delete();
        }
    }

    @Test
    public void testEventsPublishedAsJSONToFile() throws Exception {
        publisher.publish(event);
        String publishedEvents = new String(Files.readAllBytes(Paths.get(path)));
        assertTrue(publishedEvents.contains("\"a\":1"));
        assertTrue(publishedEvents.contains("\"b\":2"));
    }
}
