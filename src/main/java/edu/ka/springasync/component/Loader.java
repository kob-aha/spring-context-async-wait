package edu.ka.springasync.component;

import edu.ka.springasync.event.ComponentLoadedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class Loader {

    public static final int WAIT_TIMEOUT_SECONDES = 20;
    @Autowired
    private AppComponent parentComponent;

    private Lock loadWaitLock;
    private Condition componentLoadedCondition;

    private Semaphore loadWaitSemaphore;

    public Loader() {
        loadWaitLock = new ReentrantLock();
        componentLoadedCondition = loadWaitLock.newCondition();
        loadWaitSemaphore = new Semaphore(0);
    }

    public void loadApp() {
        System.out.println("Start loading parent component");
        parentComponent.loadComponent();

        boolean parentFinishedLoading = waitUntilLoaded();

        if (parentFinishedLoading) {
            System.out.println("Parent finished loading");
        } else {
            System.out.println(" ********** ERROR: Parent didn't finish loading within the time limit ********** ");
        }
    }

    private void sleep() {
        System.out.println("Start sleeping in Loader");

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(30));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Finished sleeping in Loader");
    }

    private boolean waitUntilLoadedCondition() {
        boolean retVal = false;
        try {
            loadWaitLock.lock();
            retVal = componentLoadedCondition.await(WAIT_TIMEOUT_SECONDES, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            loadWaitLock.unlock();
        }

        return retVal;
    }

    private boolean waitUntilLoadedSemaphore() {
        boolean retVal = false;
        try {
            retVal = loadWaitSemaphore.tryAcquire(WAIT_TIMEOUT_SECONDES, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    @EventListener
    public void onEvent(ComponentLoadedEvent event) {
//        notifyUsingCondition();
        notifyUsingSemaphore();
    }

    private void notifyUsingCondition() {
        try {
            loadWaitLock.lock();
            componentLoadedCondition.signal();
        } finally {
            loadWaitLock.unlock();
        }
    }

    private void notifyUsingSemaphore() {
        loadWaitSemaphore.release();
    }
}
