package org.kie.kogito.research.processes.core.impl;

import org.kie.kogito.research.application.api.Id;
import org.kie.kogito.research.application.api.impl.SimpleEvent;
import org.kie.kogito.research.processes.api.ProcessEvent;
import org.kie.kogito.research.processes.api.messages.ProcessMessages;

public class SimpleProcessEvent extends SimpleEvent implements ProcessEvent {
    protected SimpleProcessEvent(Id senderId, Id targetId, Object payload) {
        super(senderId, targetId, payload);
    }
    @Override
    public ProcessMessages.Message payload() {
        return (ProcessMessages.Message) super.payload();
    }
}
