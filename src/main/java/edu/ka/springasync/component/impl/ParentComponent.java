package edu.ka.springasync.component.impl;

import edu.ka.springasync.component.AppComponent;
import edu.ka.springasync.event.ComponentLoadedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ParentComponent implements AppComponent {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Async
    @Override
    public void loadComponent() {
        int sleepSeconds = 10;

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Hello from parent compoenent. Waited for " + sleepSeconds + " seconds. Publish loaded event.");

        eventPublisher.publishEvent(new ComponentLoadedEvent());
    }
}
