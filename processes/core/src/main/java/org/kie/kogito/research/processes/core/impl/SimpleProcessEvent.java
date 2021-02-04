package org.kie.kogito.research.processes.core.impl;

import org.kie.kogito.research.application.api.Id;
import org.kie.kogito.research.application.api.impl.SimpleEvent;
import org.kie.kogito.research.processes.api.ProcessEvent;

public class SimpleProcessEvent extends SimpleEvent implements ProcessEvent {
    protected SimpleProcessEvent(Id senderId, Id targetId, Object payload) {
        super(senderId, targetId, payload);
    }



}
