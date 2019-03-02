package edu.ka.springasync.config;

import edu.ka.springasync.component.AppComponent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@ComponentScan(basePackageClasses = {AppComponent.class})
public class SpringAsyncWaitConfig {
}
