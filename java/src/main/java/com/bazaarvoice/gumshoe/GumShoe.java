package com.bazaarvoice.gumshoe;

import java.util.Stack;

/**
 * Class of objects that an application uses to interact with Gumshoe.  Defines
 * the DSL by which an application can use to manipulate contexts and emit events.
 *
 * @author lance.woodson
 *
 */
public class GumShoe {
    private static Configuration configuration = new Configuration();
    private static ThreadLocal<GumShoe> instances = new ThreadLocal<GumShoe>();

    /**
     * Configure GumeShoe with a configuration object
     *
     * @param configuration
     */
    public static void configure(Configuration configuration) {
        GumShoe.configuration = configuration;
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
     * Get the thread-local instance of GumShoe.  Should be the
     * way any applications retrieve an instance.
     *
     * @return
     */
    public static GumShoe get() {
        GumShoe instance = instances.get();
        if (instances.get() == null) {
            EventFactory eventFactory = new EventFactory();
            EventHandler eventHandler = new EventHandler(getConfiguration());
            instance = new GumShoe(eventFactory, eventHandler);
            instances.set(instance);
        }
        return instance;
    }

    public static void clear() {
        instances.remove();
    }

    private EventFactory eventFactory;
    private EventHandler eventHandler;
    private Stack<Context> contexts;

    private GumShoe(EventFactory eventFactory, EventHandler eventHandler) {
        this.eventFactory = eventFactory;
        this.eventHandler = eventHandler;
        contexts = new Stack<Context>();
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
            context = new Context(name, eventFactory, eventHandler);
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
     * Finish all stacked contexts
     */
    public void finish() {
        while(!contexts.isEmpty()) {
            contexts.pop().finish();
        }
    }

    /**
     * Emit an event of the specified type that will include data
     * that has been stored in the stack of contexts
     *
     * @param type
     */
    public void emit(String type) {
        eventHandler.handle(eventFactory.constructEvent(type));
    }
}
