package org.kie.kogito.research.application.api;

public interface Application {
    <T extends UnitContainer> T get(Class<T> ctr);

    void send(Event event);
}
