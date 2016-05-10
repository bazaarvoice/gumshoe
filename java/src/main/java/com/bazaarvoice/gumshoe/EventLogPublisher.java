package com.bazaarvoice.gumshoe;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.google.gson.Gson;

public class EventLogPublisher implements Publisher {
    private String path;
    private FileHandler fileHandler;
    private Formatter formatter;
    private Logger logger;
    private Gson gson;

    public EventLogPublisher(String path) {
        try {
            this.path = path;
            fileHandler = new FileHandler(path, true);
            formatter = buildFormatter();
            fileHandler.setFormatter(formatter);
            logger = Logger.getLogger("gumshoe");
            logger.setUseParentHandlers(false);
            logger.addHandler(fileHandler);
            gson = new Gson();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void publish(Map<String, Object> event) {
        logger.info(gson.toJson(event));
    }

    public String getPath() {
        return path;
    }

    private Formatter buildFormatter() {
        return new Formatter() {
            @Override
            public String format(LogRecord record) {
                return String.format("%s\n", record.getMessage());
            }
        };
    }

    public static void main(String[] args) {
        EventLogPublisher publisher = new EventLogPublisher("test.json");
        Map<String, Object> event = new HashMap<String, Object>();
        event.put("a", 1);
        event.put("b", 2);
        publisher.publish(event);
    }
}
