package org.kie.kogito.research.application.api.impl;

import org.kie.kogito.research.application.api.Context;
import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.ExecutionModel;
import org.kie.kogito.research.application.api.MessageBus;
import org.kie.kogito.research.application.api.Unit;
import org.kie.kogito.research.application.api.UnitInstance;
import org.kie.kogito.research.application.api.UnitInstanceId;

public abstract class AbstractUnitInstance implements UnitInstance {

    private final UnitInstanceId id;
    private final Unit unit;
    private final Context context;

    public AbstractUnitInstance(UnitInstanceId id, Unit unit, Context context) {
        this.id = id;
        this.unit = unit;
        this.context = context;
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
    public Context context() {
        return context;
    }

    @Override
    public MessageBus<? extends Event> messageBus() {
        return this::send;
    }

    protected void send(Event event) {
        if (context instanceof ExecutionModel) {
            ExecutionModel executionModel = (ExecutionModel) this.context;
            if (event.targetId() == null ||
                    event.targetId().equals(this.unit.id()) ||
                    event.targetId().equals(this.id())) {
                executionModel.onEvent(event);
            }
        }
    }
}
