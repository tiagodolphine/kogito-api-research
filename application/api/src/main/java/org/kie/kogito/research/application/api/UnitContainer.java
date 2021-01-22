package org.kie.kogito.research.application.api;

public interface UnitContainer {
    Application application();

    Unit get(UnitId unitId);

    void send(Event event);
}
