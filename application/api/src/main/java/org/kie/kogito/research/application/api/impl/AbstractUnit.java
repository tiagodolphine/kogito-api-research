package org.kie.kogito.research.application.api.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.kie.kogito.research.application.api.*;

public abstract class AbstractUnit<I extends UnitId, U extends UnitInstance> implements Unit {

    private final UnitContainer container;
    private final I id;
    private final Map<UnitInstanceId, U> instances = new HashMap<>();

    public AbstractUnit(UnitContainer container, I id) {
        this.container = container;
        this.id = id;
    }

    @Override
    public Application application() {
        return container.application();
    }

    @Override
    public I id() {
        return id;
    }

    protected U register(U instance) {
        instances.put(instance.id(), instance);
        return instance;
    }

    @Override
    public MessageBus<? extends Event> messageBus() {
        return this::send;
    }

    public Collection<U> instances() {
        return instances.values();
    }

    public void send(Event event) {
        for (UnitInstance instance : instances.values()) {
            if (event.targetId() == null ||
                    event.targetId().equals(this.id()) ||
                    event.targetId() instanceof UnitInstanceId) {
                MessageBus<Event> messageBus = (MessageBus<Event>) instance.messageBus();
                messageBus.send(event);
            }
        }
    }
}
