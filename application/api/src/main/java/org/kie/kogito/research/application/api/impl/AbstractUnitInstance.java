package org.kie.kogito.research.application.api.impl;

import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.Unit;
import org.kie.kogito.research.application.api.UnitInstance;
import org.kie.kogito.research.application.api.UnitInstanceId;

public abstract class AbstractUnitInstance implements UnitInstance {

    private final UnitInstanceId id;
    private final Unit unit;

    public AbstractUnitInstance(UnitInstanceId id, Unit unit) {
        this.id = id;
        this.unit = unit;
    }

    @Override
    public UnitInstanceId id() {
        return id;
    }

    @Override
    public Unit unit() {
        return unit;
    }

    @Override
    public void send(Event event) {

    }
}
