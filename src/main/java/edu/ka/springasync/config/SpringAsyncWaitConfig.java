package edu.ka.springasync.config;

import edu.ka.springasync.component.AppComponent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {AppComponent.class})
public class SpringAsyncWaitConfig {
}
