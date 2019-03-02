package edu.ka.springasync.logic.impl;

import edu.ka.springasync.event.ComponentLoadedEvent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * IMPORTANT: This class is just for sample and might not work properly in case the executed code finish running very fast.
 * In this situation an event might be triggered before we will await on the lock.
 *
 * It is better to always use {@code SynchronousExecutorSemaphore}.
 */
public class SynchronousExecutorCondition extends SynchronousExecutorAbstract {

    private Lock loadWaitLock;
    private Condition componentLoadedCondition;

    public SynchronousExecutorCondition(ComponentLoadedEvent event) {
        super(event);

        loadWaitLock = new ReentrantLock();
        componentLoadedCondition = loadWaitLock.newCondition();
    }

    @Override
    protected boolean waitUntilEventArrive(long waitTime, TimeUnit waitTimeUnit) throws InterruptedException {
        boolean retVal = false;
        try {
            loadWaitLock.lock();
            retVal = componentLoadedCondition.await(waitTime, waitTimeUnit);
        } finally {
            loadWaitLock.unlock();
        }

        return retVal;
    }

    @Override
    protected void notifyEvent() {
        try {
            loadWaitLock.lock();
            componentLoadedCondition.signal();
        } finally {
            loadWaitLock.unlock();
        }
    }
}
