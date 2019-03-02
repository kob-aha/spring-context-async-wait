package edu.ka.springasync.logic.impl;

import edu.ka.springasync.event.ComponentLoadedEvent;
import edu.ka.springasync.logic.VoidConsumer;
import org.springframework.context.ApplicationListener;

import java.util.concurrent.TimeUnit;

public abstract class SynchronousExecutorAbstract implements ApplicationListener<ComponentLoadedEvent> {

    private boolean isWaitExecuted;
    protected final ComponentLoadedEvent event;

    public SynchronousExecutorAbstract(ComponentLoadedEvent event) {
        this.event = event;
        this.isWaitExecuted = false;
    }

    @Override
    public void onApplicationEvent(ComponentLoadedEvent event) {
        if (this.event.equals(event)) {
            System.out.println("Got event in async wait. Event: " + event);
            notifyEvent();
        }
    }

    public boolean waitForEvents(VoidConsumer function, long timeout, TimeUnit timeoutUnit) {
        if (isWaitExecuted) {
            throw new IllegalStateException("Class already executed.");
        }

        isWaitExecuted = true;
        boolean retVal = false;

        System.out.println("About to run function in " + this.getClass().getSimpleName() + ". Function: " + function);

        function.accept();

        try {
            retVal = waitUntilEventArrive(timeout, timeoutUnit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Finished running function and waiting for event in " + this.getClass().getSimpleName() +
                ". Event: " + event);

        return retVal;
    }

    protected abstract boolean waitUntilEventArrive(long waitTime, TimeUnit waitTimeUnit) throws InterruptedException;

    protected abstract void notifyEvent();
}
