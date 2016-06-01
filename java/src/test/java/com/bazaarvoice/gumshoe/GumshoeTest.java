package com.bazaarvoice.gumshoe;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.times;

public class GumshoeTest extends Assert {
    private Filter filter;
    private Decorator decorator;
    private Publisher publisher;
    private SimpleConfiguration configuration;

    @BeforeMethod
    public void setUp() {
        Gumshoe.clear();

        filter = mock(Filter.class);
        decorator = mock(Decorator.class);
        publisher = mock(Publisher.class);

        configuration = new SimpleConfiguration("test");
        configuration.setFilter(filter);
        configuration.setDecorator(decorator);
        configuration.setPublisher(publisher);
    }

    @Test(expectedExceptions={IllegalStateException.class})
    public void ensureAttemptToAccessGumshoeBeforeConfiguringItFails() {
        Gumshoe.get();
    }

    @Test
    public void ensureGetProvidesThreadLocalAccessToGumshoeInstance() throws Exception {
        final List<Gumshoe> gumshoes = new ArrayList<Gumshoe>();

        Gumshoe.configure(configuration);
        for (int i = 0; i < 2; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    gumshoes.add(Gumshoe.get());
                }
            }).start();
        }
        Thread.sleep(10);

        assertNotEquals(gumshoes.get(0), gumshoes.get(1));
    }

    @Test
    public void ensureClearResetsGumshoe() {
        Gumshoe.configure(configuration);
        Gumshoe.get();

        Gumshoe.clear();

        assertNull(Gumshoe.configuration);
        assertNull(Gumshoe.instances.get());
    }

    @Test
    public void ensureContextWithStreamIdAndNameEstablishedNewContext() {
        String name = "ctx1";
        UUID streamId = UUID.randomUUID();

        Gumshoe.configure(configuration);
        Context context = Gumshoe.get().context(streamId, name);

        assertEquals(context.getStreamId(), streamId);
        assertEquals(context.getName(), name);
        assertEquals(Gumshoe.get().contexts.pop(), context);
    }

    @Test(expectedExceptions={IllegalStateException.class})
    public void ensureContextWithStreamIdAndNameFailsIfContextAlreadyEstablished() {
        Gumshoe.configure(configuration);
        Gumshoe.get().context("test");
        Gumshoe.get().context(UUID.randomUUID(), "test2");
    }

    @Test
    public void ensureContextWithNameCanEstablishNewContext() {
        Gumshoe.configure(configuration);
        Context context = Gumshoe.get().context("test");
        assertNotNull(context);
        assertEquals(context, Gumshoe.get().contexts.pop());
    }

    @Test
    public void ensureContextWithNameCanNewContextOntoStack() {
        Gumshoe.configure(configuration);
        Context ctx1 = Gumshoe.get().context("ctx1");
        Context ctx2 = Gumshoe.get().context("ctx1");
        assertEquals(ctx2, Gumshoe.get().contexts.pop());
        assertEquals(ctx1, Gumshoe.get().contexts.pop());
        assertTrue(Gumshoe.get().contexts.isEmpty());
    }

    @Test
    public void ensureContextWithNoArgsReturnsTopOfContextStack() {
        Gumshoe.configure(configuration);
        Context ctx = Gumshoe.get().context("ctx1");
        assertEquals(Gumshoe.get().context(), ctx);
    }

    @Test
    public void ensureFinishFinishesAllContexts() {
        Gumshoe.configure(configuration);
        Context ctx1 = Gumshoe.get().context("ctx1").start();
        Context ctx2 = Gumshoe.get().context("ctx1").start();
        Gumshoe.get().finish();

        assertEquals(ctx1.getStatus(), Context.Status.FINISHED);
        assertEquals(ctx2.getStatus(), Context.Status.FINISHED);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void ensureEmitEmitsAnEvent() {
        when(filter.shouldDispatch(anyMap())).thenReturn(true);

        Gumshoe.configure(configuration);
        Gumshoe.get().context("ctx1").start();
        Gumshoe.get().emit("test");

        //  one start event, one emit event
        verify(publisher, times(2)).publish(anyMap());
    }

    @Test(expectedExceptions={ContextStackDepthException.class})
    public void ensureMaxContextStackDepthEnforcedWhenCreatingContextByName() {
        Gumshoe.configure(configuration);
        for (int i = 0; i < Gumshoe.getConfiguration().getMaxContextStackDepth() + 1; i++) {
            Gumshoe.get().context("ctx" + i);
        }
    }
}
