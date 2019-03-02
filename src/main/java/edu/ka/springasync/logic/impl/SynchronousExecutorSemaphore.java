package edu.ka.springasync.logic.impl;

import edu.ka.springasync.event.ComponentLoadedEvent;
import edu.ka.springasync.logic.VoidConsumer;
import org.springframework.context.ApplicationListener;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SynchronousExecutorSemaphore implements ApplicationListener<ComponentLoadedEvent> {

    private Semaphore loadWaitSemaphore;
    private final ComponentLoadedEvent event;
    private final int eventsNum;

    public SynchronousExecutorSemaphore(int eventsNum, ComponentLoadedEvent event) {
        this.event = event;
        this.eventsNum = eventsNum;
    }

    public boolean waitForEvents(VoidConsumer function, long timeout, TimeUnit timeoutUnit) {
        boolean retVal = false;

        // We need 1 permit to release the lock so this is why we deduced the number of components from 1.
        // Once all components finish loading, number of permits will be equal to 1 (bigger than 0) which will release the semaphore
        loadWaitSemaphore = new Semaphore(1 - eventsNum);

        System.out.println("About to run function in " + this.getClass().getSimpleName() + ". Function: " + function);

        function.accept();

        try {
            retVal = loadWaitSemaphore.tryAcquire(timeout, timeoutUnit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Finished running function and waiting for event in " + this.getClass().getSimpleName() +
                ". Event: " + event);

        return retVal;
    }

    @Override
    public void onApplicationEvent(ComponentLoadedEvent event) {
        if (this.event.equals(event)) {
            System.out.println("Got event in async wait. Event: " + event);
            loadWaitSemaphore.release();
        }
    }
}
