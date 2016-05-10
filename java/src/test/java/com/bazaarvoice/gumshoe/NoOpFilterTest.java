package com.bazaarvoice.gumshoe;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NoOpFilterTest extends Assert {
    private Filter filter;

    @BeforeMethod
    public void setUp() {
        filter = new NoOpFilter();
    }

    @Test
    public void ensureAllEventsPassed() {
        assertTrue(filter.shouldDispatch(null));
    }
}
