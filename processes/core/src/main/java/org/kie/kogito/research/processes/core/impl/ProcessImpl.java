package org.kie.kogito.research.processes.core.impl;

import org.kie.kogito.research.application.api.Context;
import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.MessageBus;
import org.kie.kogito.research.application.api.impl.AbstractUnit;
import org.kie.kogito.research.application.api.impl.LambdaMessageBus;
import org.kie.kogito.research.processes.api.*;
import org.kie.kogito.research.processes.api.Process;

public class ProcessImpl extends AbstractUnit<ProcessId, ProcessInstance> implements Process {
    private final MessageBus<ProcessEvent> messageBus;

    public ProcessImpl(ProcessContainer container, ProcessId id) {
        super(container, id);
        this.messageBus = new LambdaMessageBus<>(this::send);
    }

    public ProcessImpl(ProcessContainerImpl processContainer, SimpleProcessId id, MessageBus<? extends Event> messageBus) {
        super(processContainer, id);
        this.messageBus = (MessageBus<ProcessEvent>) messageBus;
    }

    @Override
    public MessageBus<? extends Event> messageBus() {
        return this.messageBus;
    }

    @Override
    public ProcessInstance createInstance(Context ctx) {
        var id = SimpleProcessInstanceId.create();
        return register(new ProcessInstanceImpl(id, this, ctx));
    }
}
