package org.kie.kogito.research.application.api;

public interface UnitContainer {
    Unit get(Class<? extends Context> unit);

    void send(Event event);
}
