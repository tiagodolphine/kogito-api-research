package org.kie.kogito.research.application.api;

public interface Unit {
    UnitId id();
    UnitInstance createInstance(Context ctx);
    void send(Event event);
}