package edu.ka.springasync.config;

import edu.ka.springasync.component.impl.ParentComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class SpringAsyncWaitConfig {

    @Bean
    public ParentComponent parentComponent1() {
        return new ParentComponent(3);
    }

    @Bean
    public ParentComponent parentComponent2() {
        return new ParentComponent(8);
    }
}
