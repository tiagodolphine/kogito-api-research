package org.kie.kogito.research.application.api.impl;

import java.util.HashMap;
import java.util.Map;

import org.kie.kogito.research.application.api.Application;
import org.kie.kogito.research.application.api.Context;
import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.Unit;
import org.kie.kogito.research.application.api.UnitContainer;

public class AbstractUnitContainer<U extends Unit> implements UnitContainer {
    private final Map<Class<?>, U> units = new HashMap<>();
    private final Application app;

    public AbstractUnitContainer(Application app) {
        this.app = app;
    }

    protected <T extends U> void register(T unit) {
        units.put(unit.getClass(), unit);
    }

    @Override
    public U get(Class<? extends Context> unit) {
        return units.get(unit);
    }

    @Override
    public void send(Event event) {

    }
}
