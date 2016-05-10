package com.bazaarvoice.gumshoe;

import java.util.Map;

/**
 * Publisher that does nothing; events are simply discarded.  Might be
 * useful for unit test builds, etc...
 *
 * @author lance.woodson
 *
 */
public class DevNullPublisher implements Publisher {
   @Override
    public void publish(Map<String, Object> event) {
       // do nothing
    }
}
