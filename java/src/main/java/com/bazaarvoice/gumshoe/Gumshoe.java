package com.bazaarvoice.gumshoe;

import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Class of objects that an application uses to interact with Gumshoe.  Defines
 * the DSL by which an application can use to manipulate contexts and emit events.
 *
 * @author lance.woodson
 *
 */
public class Gumshoe {
    static Configuration configuration;
    static ThreadLocal<Gumshoe> instances = new ThreadLocal<Gumshoe>();

    /**
     * Configure GumeShoe with a configuration object
     *
     * @param configuration
     */
    public static void configure(Configuration configuration) {
        Gumshoe.configuration = configuration;
    }

    /**
     * Get the configuration object, using a default if none
     * was specified
     *
     * @return
     */
    public static Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Get the thread-local instance of Gumshoe.  Should be the
     * way any applications retrieve an instance.
     *
     * @return
     */
    public static Gumshoe get() {
        if (configuration == null) {
            throw new IllegalStateException("You need to initialize Gumshoe");
        }

        Gumshoe instance = instances.get();
        if (instance == null) {
            EventFactory eventFactory = new EventFactory();
            EventHandler eventHandler = new EventHandler(getConfiguration());
            instance = new Gumshoe(eventFactory, eventHandler);
            instances.set(instance);
        }
        return instance;
    }

    public static void clear() {
        Gumshoe.configuration = null;
        Gumshoe.instances = new ThreadLocal<Gumshoe>();
        instances.remove();
    }

    EventFactory eventFactory;
    EventHandler eventHandler;
    Stack<Context> contexts;

    private Gumshoe(EventFactory eventFactory, EventHandler eventHandler) {
        this.eventFactory = eventFactory;
        this.eventHandler = eventHandler;
        contexts = new Stack<Context>();
    }

    /**
     * Create a context with the given streamId and name
     *
     * @param streamId
     * @param name
     * @return
     */
    public Context context(UUID streamId, String name) {
        if (!contexts.isEmpty()) {
            throw new IllegalStateException("StreamId already set in within previous context");
        }

        Context context = new Context(this, streamId, name, eventFactory, eventHandler);
        contexts.push(context);
        return context;
    }

    /**
     * Create a context with the given name
     *
     * @param name
     * @return
     */
    public Context context(String name) {
        Context context;

        if (contexts.isEmpty()) {
            context = new Context(this, name, eventFactory, eventHandler);
        } else {
            context = new Context(name, contexts.peek());
        }
        contexts.push(context);

        return context;
    }

    /**
     * Retrieve the current context
     *
     * @return
     */
    public Context context() {
        return contexts.peek();
    }

    /**
     * Executes the Callable within the identified and named context and returns the
     * result. If the callable executes normally, the context will be finished
     * immediately before the return.  If the callable raises an exception, the context
     * will be failed.
     *
     * @param name
     * @param logic
     * @return
     */
    public <T> T inContext(UUID streamId, String name, Callable<T> logic) throws Exception {
        if (streamId == null) {
            context(name).start();
        } else {
            context(streamId, name).start();
        }

        try {
            T result = logic.call();
            context().finish();
            return result;
        } catch (Exception e) {
            context().fail();
            throw e;
        }
    }

    /**
     * Executes the Callable within the named context and returns the result.
     * If the callable executes normally, the context will be finished immediately
     * before the return.  If the callable raises an exception, the context will
     * be failed.
     *
     * @param name
     * @param logic
     * @return
     */
    public <T> T inContext(String name, Callable<T> logic) throws Exception {
        return inContext(null, name, logic);
    }

    Context pop() {
        if (contexts.isEmpty()) {
            return null;
        } else {
            return contexts.pop();
        }
    }

    /**
     * Finish all stacked contexts
     */
    public void finish() {
        while(!contexts.isEmpty()) {
            contexts.peek().finish();
        }
    }

    /**
     * Emit an event of the specified type that will include data
     * that has been stored in the stack of contexts
     *
     * @param type
     */
    public void emit(String type) {
        contexts.peek().emit(type);
    }
}
