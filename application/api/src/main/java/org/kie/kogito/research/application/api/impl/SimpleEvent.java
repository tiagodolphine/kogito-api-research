package org.kie.kogito.research.application.api.impl;

import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.Id;

public class SimpleEvent implements Event {

    private final Id senderId;
    private final Id targetId;
    private final Object payload;

    public static SimpleEvent of(Id senderId, Id targetId, Object payload) {
        return new SimpleEvent(senderId, targetId, payload);
    }

    protected SimpleEvent(Id senderId, Id targetId, Object payload) {
        this.senderId = senderId;
        this.targetId = targetId;
        this.payload = payload;
    }

    @Override
    public Id senderId() {
        return senderId;
    }

    @Override
    public Id targetId() {
        return targetId;
    }

    public Object payload() {
        return payload;
    }
}
