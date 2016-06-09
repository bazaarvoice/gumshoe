package com.bazaarvoice.gumshoe;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.gson.Gson;

public class EventLogPublisherTest extends Assert {
    private String path;
    private EventLogPublisher publisher;
    private Map<String, Object> event;
    private Gson gson;

    @BeforeMethod
    public void setUp() {
        path = "test.log";
        publisher = new EventLogPublisher(path);
        event = new HashMap<String, Object>();
        event.put("a", 1);
        event.put("b", 2);
        gson = new Gson();
    }

    @AfterMethod
    public void tearDown() {
        publisher.close();
        File logFile = new File(path);
        if (logFile.exists()) {
            logFile.delete();
        }
    }

    @Test
    public void testEventsPublishedAsJSONToFile() throws Exception {
        publisher.publish(event);
        Map<String, Object> event = gson.fromJson(new String(Files.readAllBytes(Paths.get(path))), Map.class);
        assertEquals(event.get("a"), 1.0);
        assertEquals(event.get("b"), 2.0);
    }
   
    @Test
    public void ensureClassesSerializedCorrectly() throws Exception {
        event.put("exception", new Exception().getClass());
        publisher.publish(event);
        String publishedEvent = new String(Files.readAllBytes(Paths.get(path)));
        gson.fromJson(publishedEvent, Map.class);
    }
}
