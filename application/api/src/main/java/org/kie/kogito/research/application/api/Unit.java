package org.kie.kogito.research.application.api;

public interface Unit<C extends Context> {
    UnitInstance<C> createInstance(C ctx);
}