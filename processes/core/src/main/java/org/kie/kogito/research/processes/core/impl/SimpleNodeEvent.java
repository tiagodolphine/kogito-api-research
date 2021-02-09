package org.kie.kogito.research.processes.core.impl;

import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.Id;
import org.kie.kogito.research.application.api.impl.SimpleEvent;
import org.kie.kogito.research.processes.api.NodeEvent;
import org.kie.kogito.research.processes.api.ProcessEvent;

public class SimpleNodeEvent extends SimpleEvent implements NodeEvent,
                                                            Event {
    public SimpleNodeEvent(Id senderId, Id targetId, Object payload) {
        super(senderId, targetId, payload);
    }
}
