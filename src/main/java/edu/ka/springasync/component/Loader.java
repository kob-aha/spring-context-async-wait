package edu.ka.springasync.component;

import edu.ka.springasync.component.impl.ParentComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Loader {

    @Autowired
    private ParentComponent parentComponent;

    public void loadApp() {
        System.out.println("Start loading parent component");
        parentComponent.loadComponent();
        System.out.println("Finished loading parent component");
    }
}
