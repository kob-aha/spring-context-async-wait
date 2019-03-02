package edu.ka.springasync.component;

import edu.ka.springasync.event.ComponentId;
import edu.ka.springasync.event.ComponentLoadedEvent;
import edu.ka.springasync.logic.SynchornousExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class Loader {

    public static final int WAIT_TIMEOUT_SECONDES = 50;

    @Autowired
    private List<AppComponent> parentComponent;

    @Autowired
    private SynchornousExecutor synchornousExecutor;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private Lock loadWaitLock;
    private Condition componentLoadedCondition;

    public Loader() {
        loadWaitLock = new ReentrantLock();
        componentLoadedCondition = loadWaitLock.newCondition();
    }

    @Async
    public void loadApp() {
        boolean parentFinishedLoading = synchornousExecutor.runAndWaitForEvents(this::doLoadApp,
                parentComponent.size(),
                new ComponentLoadedEvent(ComponentId.PARENT),
                WAIT_TIMEOUT_SECONDES,
                TimeUnit.SECONDS);

        if (parentFinishedLoading) {
            System.out.println("Parent finished loading");

            eventPublisher.publishEvent(new ComponentLoadedEvent(ComponentId.LOADER));
        } else {
            System.out.println(" ********** ERROR: Parent didn't finish loading within the time limit ********** ");
        }
    }

    private void doLoadApp() {

        System.out.println("Start loading " + parentComponent.size() + " parent components");

        for (AppComponent appComponent : parentComponent) {
            appComponent.loadComponent();
        }

//        sleep();

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

    private void notifyUsingCondition() {
        try {
            loadWaitLock.lock();
            componentLoadedCondition.signal();
        } finally {
            loadWaitLock.unlock();
        }
    }
}