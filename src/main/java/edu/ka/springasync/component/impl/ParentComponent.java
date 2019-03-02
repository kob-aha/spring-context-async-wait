package edu.ka.springasync.component.impl;

import edu.ka.springasync.component.AppComponent;
import edu.ka.springasync.event.ComponentId;
import edu.ka.springasync.event.ComponentLoadedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ParentComponent implements AppComponent {

    public static final int DEFAULT_SLEEP_SECONDS = 10;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private int sleepSeconds;

    public ParentComponent() {
        this(DEFAULT_SLEEP_SECONDS);
    }

    public ParentComponent(int sleepSeconds) {
        this.sleepSeconds = sleepSeconds;
    }

    @Async
    @Override
    public void loadComponent() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(sleepSeconds));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Hello from parent compoenent. Waited for " + sleepSeconds + " seconds. Publish loaded event.");

        eventPublisher.publishEvent(createLoadedEvent());
    }

    private ComponentLoadedEvent createLoadedEvent() {
        return new ComponentLoadedEvent(ComponentId.PARENT);
    }
}
