package org.kie.kogito.research.application.api;

public interface Event {
    Id senderId();
    Id targetId();
    Object payload();
}
