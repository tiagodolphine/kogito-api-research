package org.kie.kogito.research.application.api;

public interface UnitInstance {
    UnitInstanceId id();
    void send(Event event);

    Unit unit();
}
