package org.kie.kogito.research.processes.core.impl.runtime;

import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.MessageBus;
import org.kie.kogito.research.processes.api.NodeInstanceId;

public class TaskInstance extends AbstractNode {

    public TaskInstance() {
    }

    public TaskInstance(NodeInstanceId id, MessageBus<Event> messageBus, AbstractNode next) {
        super(id, messageBus, next);
    }
}
