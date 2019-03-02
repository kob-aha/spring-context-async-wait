package edu.ka.springasync.logic.impl;

import edu.ka.springasync.event.ComponentLoadedEvent;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SynchronousExecutorSemaphore extends SynchronousExecutorAbstract {

    private Semaphore loadWaitSemaphore;
    private final int eventsNum;

    public SynchronousExecutorSemaphore(int eventsNum, ComponentLoadedEvent event) {
        super(event);

        this.eventsNum = eventsNum;

        // We need 1 permit to release the lock so this is why we deduced the number of components from 1.
        // Once all components finish loading, number of permits will be equal to 1 (bigger than 0) which will release the semaphore
        loadWaitSemaphore = new Semaphore(1 - eventsNum);
    }

    @Override
    protected boolean waitUntilEventArrive(long waitTime, TimeUnit waitTimeUnit) throws InterruptedException {
        return loadWaitSemaphore.tryAcquire(waitTime, waitTimeUnit);
    }

    @Override
    protected void notifyEvent() {
        loadWaitSemaphore.release();
    }
}
