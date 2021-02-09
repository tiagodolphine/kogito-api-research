package org.kie.kogito.research.processes.core.impl.runtime;

import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.MessageBus;
import org.kie.kogito.research.processes.api.NodeInstanceId;

public class StartNodeInstance extends AbstractNode {

    public StartNodeInstance() {
    }

    public StartNodeInstance(NodeInstanceId id, MessageBus<Event> messageBus, AbstractNode next) {
        super(id, messageBus, next);
    }
}
