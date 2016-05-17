package com.bazaarvoice.gumshoe;

import org.testng.Assert;
import org.testng.annotations.Test;

public class AttributeTest extends Assert {
    @Test
    public void ensureAsPathConvertsManyStringsToPath() {
        assertEquals(Attribute.asPath("one", "two", "three"), "one > two > three");
    }
}
