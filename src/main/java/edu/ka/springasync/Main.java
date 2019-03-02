package edu.ka.springasync;

import edu.ka.springasync.component.Loader;
import edu.ka.springasync.component.impl.ParentComponent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext config = SpringApplication.run(Main.class, args);
        Loader loader = config.getBean(Loader.class);
        System.out.println("Start loading app");
        loader.loadApp();
        System.out.println("Finished loading app");
    }

}
