package edu.ka.springasync.logic;

import edu.ka.springasync.event.ComponentLoadedEvent;

import java.util.concurrent.TimeUnit;

public interface SynchornousExecutor {
    boolean runAndWaitForEvent(VoidConsumer function, ComponentLoadedEvent event, long timeout, TimeUnit timeoutUnit);

    boolean runAndWaitForEvents(VoidConsumer function, int numberOfEvents, ComponentLoadedEvent event, long timeout, TimeUnit timeoutUnit);
}
