package com.bazaarvoice.gumshoe;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class ContextTest extends Assert {
    private EventFactory eventFactory;
    private EventHandler eventHandler;
    private Context context;
    private Context secondContext;

    @BeforeMethod
    public void setUp() {
        Gumshoe.configure(new SimpleConfiguration("test"));
        eventFactory = new EventFactory();
        eventHandler = mock(EventHandler.class);
        context = new Context(Gumshoe.get(), "context one", eventFactory, eventHandler);
        secondContext = new Context("context two", context);
    }

    @Test
    public void ensureContextCreationAssignsStreamId() {
        assertNotNull(context.getStreamId());
    }

    @Test
    public void ensureContextCreationSetsContext() {
        assertEquals(context.data.get(Attribute.named("context")), Attribute.asPath("context one"));
    }

    @Test
    public void ensureContextCreatedWithOtherContextPropagatesStreamId() {
        assertEquals(secondContext.getStreamId(), context.getStreamId());
    }

    @Test
    public void ensureContextCreatedWithOtherContextAppendsNameToContext() {
        assertEquals(secondContext.data.get(Attribute.named("context")), Attribute.asPath("context one", "context two"));
    }

    @Test
    public void ensureNewContextIsInCreatedStatus() {
        assertEquals(context.getStatus(), Context.Status.CREATED);
    }

    @Test
    public void ensureContextThatHasHadStartCalledIsInStartedStatus() {
        context.start();
        assertEquals(context.getStatus(), Context.Status.STARTED);
    }

    @Test
    public void ensureContextThatHasHadFinishCalledIsInFinishedStatus() {
        context.start();
        context.finish();
        assertEquals(context.getStatus(), Context.Status.FINISHED);
    }

    @Test
    public void ensureContextThatHasHadFailCalledIsInFinishedStatus() {
        context.start();
        context.fail();
        assertEquals(context.getStatus(), Context.Status.FINISHED);
    }

    @Test
    public void ensurePutStoresDataForContext() {
        context.put("foo", "bar");
        assertEquals(context.data.get("foo"), "bar");
    }

    @Test(expectedExceptions={IllegalStateException.class})
    public void ensurePutWhenInFinishedStatusFails() {
        context.start();
        context.finish();
        context.put("foo", "bar");
    }

    @Test
    public void ensureStartSendsStartedEventToDispatcher() {
        context.start();

        verify(eventHandler).handle(buildExpectedEvent(context, "started"));
    }

    @Test
    public void ensureStartLoadsEventFactorysDataStack() {
        context.put("foo", "bar").start();

        assertEquals(eventFactory.getDataStack().get("foo"), "bar");
    }

    @Test
    public void ensureStartWithCallableReturnsCallableResult() {
        String result = context.start(new Callable<String>() {
            @Override
            public String call() {
                return "test";
            }
        });
        assertEquals(result, "test");
    }

    @Test
    public void ensureStartWithCallableFinishesStatus() {
        context.start(new Callable<String>() {
            @Override
            public String call() {
                return "test";
            }
        });
        assertEquals(Context.Status.FINISHED, context.getStatus());
    }

    @Test(expectedExceptions={InContextException.class})
    public void ensureStartWithCallableThrowingExceptionPropagatesException() {
        context.start(new Callable<String>() {
            @Override
            public String call() {
                throw new RuntimeException("Ooops, I did it again");
            }
        });
    }

    @Test
    public void ensureStartWithCallableThrowingExceptionFinishesContext() {
        try {
            context.start(new Callable<String>() {
                @Override
                public String call() {
                    throw new RuntimeException("Ooops, I did it again");
                }
            });
        } catch (Exception e) { }
        assertEquals(Context.Status.FINISHED, context.getStatus());
    }

    @Test
    public void ensureStartWithVoidCallableFinishesContext() {
        context.start(new VoidCallable() {
            @Override
            public void call() {
               // do something
            }
        });
        assertEquals(Context.Status.FINISHED, context.getStatus());
    }

    @Test(expectedExceptions={InContextException.class})
    public void ensureStartWithVoidCallableThrowingExceptionPropagatesException() {
        context.start(new VoidCallable() {
            @Override
            public void call() {
                throw new RuntimeException("Ooops, I did it again");
            }
        });
    }

    @Test
    public void ensureStartWithVoidCallableThrowingExceptionFinishesContext() {
        try {
            context.start(new VoidCallable() {
                @Override
                public void call() {
                    throw new RuntimeException("Ooops, I did it again");
                }
            });
        } catch (Exception e) { }
        assertEquals(Context.Status.FINISHED, context.getStatus());
    }

    @Test(expectedExceptions={IllegalStateException.class})
    public void ensureEmitFailsIfContextNotStarted() {
        context.emit("test");
    }

    @Test
    public void ensureEmitSendsEventToDispatcher() {
        context.start();

        context.emit("test");

        verify(eventHandler).handle(buildExpectedEvent(context, "started"));
        verify(eventHandler).handle(buildExpectedEvent(context, "test"));
    }

    @Test
    public void ensureEmitContainsNewData() {
        context.start().put("foo", "bar");

        context.emit("test");

        verify(eventHandler).handle(buildExpectedEvent(context, "started"));
        Map<String, Object> expectedTestEvent = buildExpectedEvent(context, "test");
        expectedTestEvent.put("foo", "bar");
        verify(eventHandler).handle(expectedTestEvent);
    }

    @Test
    public void ensureFinishSendsFinishedEventToDispatcher() {
        context.start();
        context.put("should_not", "be_in_finished_event");
        context.finish();

        verify(eventHandler).handle(buildExpectedEvent(context, "started"));
        verify(eventHandler).handle(buildExpectedEvent(context, "finished"));
    }

    @Test
    public void ensureFinishUnloadsEventFactoryDataStack() {
        context.put("foo", "bar").start().finish();

        assertEquals(eventFactory.getDataStack().get("foo"), null);
    }

    @Test
    public void ensureFailSendsFinishedEventToDispatcher() {
        context.start();
        context.fail();

        verify(eventHandler).handle(buildExpectedEvent(context, "started"));
        verify(eventHandler).handle(buildExpectedEvent(context, "failed"));
    }

    @Test
    public void ensureFailWithExceptionSendsFailedEventWithExceptionDetails() {
        RuntimeException ex = new RuntimeException("outter", new Exception("inner"));

        context.start();
        context.fail(ex);

        Map<String, Object> failedEvent = buildExpectedEvent(context, "failed");
        failedEvent.put(Attribute.named("exception"), "java.lang.RuntimeException");
        failedEvent.put(Attribute.named("failure_message"), "outter");
        failedEvent.put(Attribute.named("root_exception"), "java.lang.Exception");
        failedEvent.put(Attribute.named("root_failure_message"), "inner");

        verify(eventHandler).handle(buildExpectedEvent(context, "started"));
        verify(eventHandler).handle(failedEvent);
    }


    @Test
    public void ensureFailUnloadsEventFactoryDataStack() {
        context.put("foo", "bar").start().fail();

        assertEquals(eventFactory.getDataStack().get("foo"), null);
    }

    private Map<String, Object> buildExpectedEvent(Context context, String type) {
        Map<String, Object> event = new HashMap<String, Object>();
        event.put(Attribute.named("stream_id"), context.getStreamId().toString());
        event.put(Attribute.named("context"), Attribute.asPath(context.getName()));
        event.put(Attribute.named("event_type"), type);

        if (type.equals("finished") || type.equals("failed")) {
            event.put(Attribute.named("elapsed"), context.getFinishedTime() - context.getStartTime());
        }

        return event;
    }
}
