package edu.ka.springasync;

import edu.ka.springasync.component.Loader;
import edu.ka.springasync.event.ComponentId;
import edu.ka.springasync.event.ComponentLoadedEvent;
import edu.ka.springasync.logic.SynchornousExecutor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext config = SpringApplication.run(Main.class, args);
        Loader loader = config.getBean(Loader.class);
        SynchornousExecutor synchornousExecutor = config.getBean(SynchornousExecutor.class);

        boolean loaderFinished = synchornousExecutor.runAndWaitForEvent(loader::loadApp,
                new ComponentLoadedEvent(ComponentId.LOADER),
                55,
                TimeUnit.SECONDS);

        if (loaderFinished) {
            System.out.println("App loaded successfully");
        } else {
            System.out.println(" ********** ERROR: Loader didn't finish loading within the time limit ********** ");
        }
    }

}
