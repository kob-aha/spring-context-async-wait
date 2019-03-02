package edu.ka.springasync.component;

import edu.ka.springasync.component.impl.ParentComponent;
import edu.ka.springasync.event.ComponentLoadedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Loader {

    @Autowired
    private AppComponent parentComponent;

    public void loadApp() {
        System.out.println("Start loading parent component");
        parentComponent.loadComponent();
    }

    @EventListener
    public void onEvent(ComponentLoadedEvent event) {
        System.out.println("Got event loaded. Parent finished loading");
    }
}
