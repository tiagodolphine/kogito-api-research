package org.kie.kogito.research.application.api.impl;

import java.util.HashMap;
import java.util.Map;

import org.kie.kogito.research.application.api.Application;
import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.Unit;
import org.kie.kogito.research.application.api.UnitContainer;
import org.kie.kogito.research.application.api.UnitId;
import org.kie.kogito.research.application.api.UnitInstance;
import org.kie.kogito.research.application.api.UnitInstanceId;

public abstract class AbstractUnit<U extends UnitInstance> implements Unit {

    private final UnitContainer container;
    private final UnitId id;
    private final Map<UnitInstanceId, U> instances = new HashMap<>();

    public AbstractUnit(UnitContainer container, UnitId id) {
        this.container = container;
        this.id = id;
    }

    @Override
    public Application application() {
        return container.application();
    }

    @Override
    public UnitId id() {
        return id;
    }

    protected U register(U instance) {
        instances.put(instance.id(), instance);
        return instance;
    }

    @Override
    public void send(Event event) {
        for (UnitInstance instance : instances.values()) {
            if (event.targetId() == null ||
                    event.targetId().equals(this.id()) ||
                    event.targetId() instanceof UnitInstanceId) {
                instance.send(event);
            }
        }
    }
}
