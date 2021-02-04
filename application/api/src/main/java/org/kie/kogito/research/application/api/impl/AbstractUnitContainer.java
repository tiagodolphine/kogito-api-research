package org.kie.kogito.research.application.api.impl;

import java.util.HashMap;
import java.util.Map;

import org.kie.kogito.research.application.api.Application;
import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.Unit;
import org.kie.kogito.research.application.api.UnitContainer;
import org.kie.kogito.research.application.api.UnitId;

public class AbstractUnitContainer<U extends Unit> implements UnitContainer {
    private final Map<UnitId, U> units = new HashMap<>();
    private final Application application;

    public AbstractUnitContainer(Application application) {
        this.application = application;
    }

    protected <T extends U> T register(T unit) {
        units.put(unit.id(), unit);
        return unit;
    }

    @Override
    public Application application() {
        return application;
    }

    @Override
    public U get(UnitId unitId) {
        return units.get(unitId);
    }

    @Override
    public void send(Event event) {
        for (U u : units.values()) {
            u.send(event);
        }
    }
}
