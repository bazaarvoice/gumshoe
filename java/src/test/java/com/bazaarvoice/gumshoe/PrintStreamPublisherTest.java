package com.bazaarvoice.gumshoe;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PrintStreamPublisherTest extends Assert {
    private ByteArrayOutputStream outputStream;
    private PrintStream printStream;
    private PrintStreamPublisher publisher;
    private Map<String, Object> event;

    @BeforeMethod
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        printStream = new PrintStream(outputStream);
        publisher = new PrintStreamPublisher(printStream);
        event = new HashMap<String, Object>();
        event.put("a", 1);
        event.put("b", 2);
    }

    @Test
    public void ensurePublishSendsPrettyPrintedEventToPrintStream() {
        publisher.publish(event);
        String eventOutput = new String(outputStream.toByteArray());
        assertTrue(eventOutput.contains("a: 1"));
        assertTrue(eventOutput.contains("b: 2"));
    }
}
