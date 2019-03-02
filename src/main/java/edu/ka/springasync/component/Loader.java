package edu.ka.springasync.component;

import edu.ka.springasync.component.impl.ParentComponent;
import edu.ka.springasync.event.ComponentLoadedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class Loader {

    @Autowired
    private AppComponent parentComponent;

    private Lock loadWaitLock;
    private Condition componentLoadedCondition;

    public Loader() {
        loadWaitLock = new ReentrantLock();
        componentLoadedCondition = loadWaitLock.newCondition();
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

    private boolean waitUntilLoaded() {
        boolean retVal = false;
        try {
            loadWaitLock.lock();
            retVal = componentLoadedCondition.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            loadWaitLock.unlock();
        }

        return retVal;
    }

    @EventListener
    public void onEvent(ComponentLoadedEvent event) {
        try {
            loadWaitLock.lock();
            componentLoadedCondition.signal();
        } finally {
            loadWaitLock.unlock();
        }

    }
}
