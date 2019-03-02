package edu.ka.springasync.component.impl;

import edu.ka.springasync.component.AppComponent;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ParentComponent implements AppComponent {

    @Override
    public void loadComponent() {
        int sleepSeconds = 10;

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Hello from parent compoenent. Waited for " + sleepSeconds + " seconds");
    }
}
