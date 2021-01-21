package org.kie.kogito.research.application.api;

public interface UnitContainer {
    <C extends Context> Unit<C> get(Class<C> unit);
}
