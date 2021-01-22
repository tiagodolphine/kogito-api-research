package org.kie.kogito.research.application.api;

public interface UnitContainer {
    Unit get(UnitId unitId);

    void send(Event event);
}
