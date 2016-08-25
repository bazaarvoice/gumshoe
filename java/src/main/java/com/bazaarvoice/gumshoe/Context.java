package com.bazaarvoice.gumshoe;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * A data context for events.  Contexts can be stacked.  Data can be stored
 * within a context.  Data included in an event includes all data in the
 * stack, with data higher in the stack hiding/overriding data lower in
 * the stack.  There are two exceptions to this:  the stack's stream_id and
 * name.  The first context in a stack generates a stream ID and that value
 * is used for all contexts in the stack.  While each context has a name,
 * the list of context names in the stack is emitted with events.  The
 * context in the event is thus a path structure describing the context
 * stack.
 *
 * Contexts can be started and finished and emit start and finish events
 * automatically at these times.
 *
 * @author lance.woodson
 *
 */
public class Context {
    public static enum Status {
        CREATED, STARTED, FINISHED;
    }
    private Gumshoe gumshoe;
    private UUID streamId;
    private String name;
    private EventFactory eventFactory;
    private EventHandler eventDispatcher;
    Map<String, Object> data;
    private Long startTime;
    private Long finishedTime;

    /**
     * Construct a context with the specified name, event factory and event dispatcher.
     * A Stream ID will be generated for the context.  Used to create the first Context
     * in a stack.
     *
     * @param name
     * @param eventFactory
     * @param eventDispatcher
     */
    public Context(Gumshoe gumshoe, String name, EventFactory eventFactory, EventHandler eventDispatcher) {
        this(gumshoe, UUID.randomUUID(), name, eventFactory, eventDispatcher);
    }

    /**
     * Create a context with the specified name using the passed previous Context.  This
     * is used when constructing other contexts to place on a stack.
     *
     * @param name
     * @param previous
     */
    public Context(String name, Context previous) {
        this(previous.gumshoe, previous.streamId, name, previous.eventFactory, previous.eventDispatcher);
        this.streamId = previous.streamId;
        this.name = name;
        this.eventFactory = previous.eventFactory;
        this.eventDispatcher = previous.eventDispatcher;
        this.data = new HashMap<String, Object>();
        String previousContext = (String)previous.data.get(Attribute.named("context"));
        this.data.put(Attribute.named("context"), Attribute.asPath(previousContext, name));
        this.data.put(Attribute.named("stream_id"), streamId.toString());
    }

    /**
     * Construct a context with the specified streamId, name, event factory and event dispatcher.
     * A Stream ID will be generated for the context.  Used to create the first Context
     * in a stack when we want to propagate the streamId generated from another context, possibly
     * in another system.
     *
     * @param streamId
     * @param name
     * @param eventFactory
     * @param eventDispatcher
     */
    public Context(Gumshoe gumshoe, UUID streamId, String name, EventFactory eventFactory, EventHandler eventDispatcher) {
        this.gumshoe = gumshoe;
        this.streamId = streamId;
        this.name = name;
        this.eventFactory = eventFactory;
        this.eventDispatcher = eventDispatcher;
        this.data = new HashMap<String, Object>();

        this.data.put(Attribute.named("context"), Attribute.asPath(name));
        this.data.put(Attribute.named("stream_id"), streamId.toString());
    }

    /**
     * Return the status of the Context -- either CREATED, STARTED or FINISHED
     *
     * @return
     */
    public Status getStatus() {
        if (finishedTime != null) {
            return Status.FINISHED;
        } else if (startTime != null) {
            return Status.STARTED;
        } else {
            return Status.CREATED;
        }
    }

    /**
     * Put some data into the context
     *
     * @param name
     * @param value
     * @return
     */
    public Context put(String name, Object value) {
        if (Status.FINISHED.equals(getStatus())) {
            throw new IllegalStateException("Cannot put data into a finished context");
        }

        data.put(name, value);

        return this;
    }

    /**
     * Start the context, results in a started event being emitted for the context
     *
     * @return
     */
    public Context start() {
        if (!Status.CREATED.equals(getStatus())) {
            throw new IllegalStateException("Cannot start a context that has been started or has finished");
        }

        startTime = System.currentTimeMillis();
        eventFactory.getDataStack().push(new HashMap<String, Object>(data));
        Map<String, Object> event = eventFactory.constructEvent("started");
        eventDispatcher.handle(event);

        return this;
    }

    /**
     * Starts a context and calls the Callable within it and returns the result.  The context will
     * be finished or failed as appropriate.  If an exception bubbles out of the Callable, an
     * InContextException runtime exception will be thrown.
     *
     * @param callable
     * @return
     */
    public <V> V start(Callable<V> callable) {
        try {
            start();
            V result = callable.call();
            finish();
            return result;
        } catch (Exception exception) {
            fail(exception);
            throw new InContextException(exception);
        }
    }

    /**
     * Starts a context and calls the VoidCallable within it.  The context will be finished or
     * failed as appropriate.  If an exception bubbles out of the Callable, an InContextException
     * runtime exception will be thrown.
     *
     * @param callable
     */
    public void start(VoidCallable callable) {
        try {
            start();
            callable.call();
            finish();
        } catch (Exception exception) {
            fail(exception);
            throw new InContextException(exception);
        }
    }

    /**
     * Emits an event within the current context
     *
     * @return
     */
    public Context emit(String type) {
        if (!Status.STARTED.equals(getStatus())) {
            throw new IllegalStateException(String.format("Cannot emit events for a context that has not been started: %s", getStatus()));
        }

        Map<String, Object> event = eventFactory.constructEvent(type, data);
        eventDispatcher.handle(event);

        return this;
    }

    /**
     * Finish the context, emitting a finished event and ensure that this context's data does not
     * get added to future events.
     *
     * @return
     */
    public Context finish() {
        return finish("finished");
    }

    /**
     * Finish the context, emitting a failed event and ensure that this context's data does not
     * get added to future events.
     *
     * @return
     */
    public Context fail() {
        return finish("failed");
    }

    /**
     * Finish the context, emitting a failed event and include details about the passed exception
     * as follows:
     *
     * exception:  The exception class name
     * failure_message:  The message of the exception
     * root_exception:  The Throwable class name of the cause
     * root_failure_message:  The message of the throwable cause of the exception
     *
     * @param exception
     * @return
     */
    public Context fail(Exception exception) {
        data.put(Attribute.named("exception"), exception.getClass().getName());
        data.put(Attribute.named("failure_message"), exception.getMessage());
        if (exception.getCause() != null) {
            Throwable root = exception.getCause();
            data.put(Attribute.named("root_exception"), root.getClass().getName());
            data.put(Attribute.named("root_failure_message"), root.getMessage());
        }

        return finish("failed", data);
    }

    public UUID getStreamId() {
        return streamId;
    }

    public String getName() {
        return name;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getFinishedTime() {
        return finishedTime;
    }

    private Context finish(String eventType) {
        return finish(eventType, new HashMap<String, Object>());
    }

    private Context finish(String eventType, Map<String, Object> additionalData) {
        if (!Status.STARTED.equals(getStatus())) {
            throw new IllegalStateException(String.format("Cannot finish a context that has not been started: %s", getStatus()));
        }

        Map<String, Object> event = eventFactory.constructEvent(eventType, additionalData);
        finishedTime = System.currentTimeMillis();
        event.put(Attribute.named("elapsed"), finishedTime - startTime);
        eventDispatcher.handle(event);
        gumshoe.pop();
        eventFactory.getDataStack().pop();

        return this;
    }

    @Override
    public String toString() {
        return String.format("Context(%s)", name);
    }
}
