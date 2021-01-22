package org.kie.kogito.research.application.api.impl;

import java.util.HashMap;
import java.util.Map;

import org.kie.kogito.research.application.api.Application;
import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.UnitContainer;

public abstract class AbstractApplication implements Application {

    private final Map<Class<?>, UnitContainer> containers = new HashMap<>();

    protected <T extends UnitContainer> void register(T container) {
        containers.put(container.getClass(), container);
    }

    @Override
    public void send(Event event) {
        for (UnitContainer ctr : containers.values()) {
            ctr.send(event);
        }
    }

    @Override
    public <T extends UnitContainer> T get(Class<T> ctr) {
        return (T) containers.get(ctr);
    }
}
