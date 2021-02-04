package org.kie.kogito.research.application.api;

import java.util.function.Consumer;

public interface MessageBus<T extends Event> {
    public void send(T event);
    default public void subscribe(Consumer<T> consumer) {}
}
