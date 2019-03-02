package edu.ka.springasync.event;

import org.springframework.context.ApplicationEvent;

import java.util.Objects;

public class ComponentLoadedEvent extends ApplicationEvent {
    private final ComponentId componentId;

    public ComponentLoadedEvent(ComponentId componentId) {
        super(componentId);

        this.componentId = Objects.requireNonNull(componentId, "Component ID cannot be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComponentLoadedEvent that = (ComponentLoadedEvent) o;
        return componentId == that.componentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(componentId);
    }

    @Override
    public String toString() {
        return "ComponentLoadedEvent{" +
                "componentId=" + componentId +
                '}';
    }
}
