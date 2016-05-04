package com.bazaarvoice.gumshoe.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bazaarvoice.gumshoe.Publisher;

public class InMemoryPublisher implements Publisher {
    private List<Map<String, Object>> events;

    public InMemoryPublisher() {
        events = new ArrayList<Map<String,Object>>();
    }

    @Override
    public void publish(Map<String, Object> event) {
        events.add(event);
    }

    public List<Map<String, Object>> getEvents() {
        return events;
    }
}
