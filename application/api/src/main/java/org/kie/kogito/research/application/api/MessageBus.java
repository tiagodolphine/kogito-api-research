package org.kie.kogito.research.application.api;

public interface MessageBus<T extends Event> {
    public void send(T event);
}
