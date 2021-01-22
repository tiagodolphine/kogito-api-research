package org.kie.kogito.research.application.api.impl;

import java.util.HashMap;
import java.util.Map;

import org.kie.kogito.research.application.api.Application;
import org.kie.kogito.research.application.api.Context;
import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.Unit;
import org.kie.kogito.research.application.api.UnitContainer;
import org.kie.kogito.research.application.api.UnitId;

public class AbstractUnitContainer<U extends Unit> implements UnitContainer {
    private final Map<UnitId, U> units = new HashMap<>();
    private final Application app;

    public AbstractUnitContainer(Application app) {
        this.app = app;
    }

    protected <T extends U> void register(T unit) {
        units.put(unit.id(), unit);
    }

    @Override
    public U get(UnitId unitId) {
        return units.get(unitId);
    }

    @Override
    public void send(Event event) {

    }
}
