package com.bazaarvoice.gumshoe;

import java.util.Map;

/**
 * Filter that allows all events through
 *
 * @author lance.woodson
 *
 */
public class NoOpFilter implements Filter {
    @Override
    public boolean shouldDispatch(Map<String, Object> event) {
        return true;
    }
}
