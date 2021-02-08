package org.kie.kogito.research.application.api;

public interface UnitInstance {
    UnitInstanceId id();
    Unit unit();
    Context context();
    MessageBus<? extends Event> messageBus();
    void run();
}
