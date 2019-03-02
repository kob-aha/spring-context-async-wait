package edu.ka.springasync.logic;

import edu.ka.springasync.event.ComponentLoadedEvent;

import java.util.concurrent.TimeUnit;

public interface SynchornousExecutor {
    default boolean runAndWaitForEvent(VoidConsumer function, ComponentLoadedEvent event, long timeout, TimeUnit timeoutUnit) {
        return runAndWaitForEvents(function, 1, event, timeout, timeoutUnit);
    }

    boolean runAndWaitForEvents(VoidConsumer function, int numberOfEvents, ComponentLoadedEvent event, long timeout, TimeUnit timeoutUnit);
}
