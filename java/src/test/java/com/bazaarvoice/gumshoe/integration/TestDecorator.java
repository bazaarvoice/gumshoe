package com.bazaarvoice.gumshoe.integration;

import java.util.Map;

import com.bazaarvoice.gumshoe.Decorator;

public class TestDecorator implements Decorator {
    @Override
    public Map<String, Object> decorate(Map<String, Object> event) {
        event.put("test", true);
        return event;
    }
}
