package edu.ka.springasync.logic.impl;

import edu.ka.springasync.event.ComponentLoadedEvent;
import edu.ka.springasync.logic.SynchornousExecutor;
import edu.ka.springasync.logic.VoidConsumer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class SynchronousExecutorSpringAdapter implements SynchornousExecutor, ApplicationContextAware {

    private ApplicationEventMulticaster eventMulticaster;

    @Override
    public boolean runAndWaitForEvent(VoidConsumer function, ComponentLoadedEvent event, long timeout, TimeUnit timeoutUnit) {
        SynchronousExecutorAbstract asyncWait = new SynchronousExecutorCondition(event);
        eventMulticaster.addApplicationListener(asyncWait);
        return asyncWait.waitForEvents(function, timeout, timeoutUnit);
    }

    @Override
    public boolean runAndWaitForEvents(VoidConsumer function, int numberOfEvents, ComponentLoadedEvent event, long timeout, TimeUnit timeoutUnit) {
        SynchronousExecutorAbstract asyncWait = new SynchronousExecutorSemaphore(numberOfEvents, event);
        eventMulticaster.addApplicationListener(asyncWait);
        return asyncWait.waitForEvents(function, timeout, timeoutUnit);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationEventMulticaster eventMulticaster = applicationContext.getBean(ApplicationEventMulticaster.class);
        this.eventMulticaster = Objects.requireNonNull(eventMulticaster, "Spring's ApplicationEventMulticaster cannot be null");
    }
}
