package org.kie.kogito.research.processes.core.impl;

import org.kie.kogito.research.application.api.Context;
import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.MessageBus;
import org.kie.kogito.research.application.api.impl.AbstractUnit;
import org.kie.kogito.research.application.api.impl.LambdaMessageBus;
import org.kie.kogito.research.processes.api.*;
import org.kie.kogito.research.processes.api.Process;
import org.kie.kogito.research.processes.api.messages.ProcessMessages;

import java.util.ArrayDeque;
import java.util.Deque;

public class ProcessImpl extends AbstractUnit<ProcessId, ProcessInstance> implements Process {
    private final MessageBus<ProcessEvent> messageBus;
    private final Deque<Event> events;

    public ProcessImpl(ProcessContainer container, ProcessId id) {
        super(container, id);
        this.messageBus = new LambdaMessageBus<>(this::send);
        this.events = null;
    }

    public ProcessImpl(ProcessContainerImpl processContainer, SimpleProcessId id, MessageBus<? extends Event> messageBus) {
        super(processContainer, id);
        this.messageBus = (MessageBus<ProcessEvent>) messageBus;
        this.events = new ArrayDeque<>();
        messageBus.subscribe(this::enqueue);
    }

    @Override
    public MessageBus<? extends Event> messageBus() {
        return this.messageBus;
    }

    public void run() {
        for (Event event = events.poll(); event != null; event = events.poll()) {
            receive(event);
        }
    }

    protected void receive(Event event) {
        // internal handling logic
        if (event.payload() instanceof ProcessMessages.CreateInstance) {
            var createInstance = (ProcessMessages.CreateInstance) event.payload();
            var instance = this.createInstance(new SimpleProcessContext());
            messageBus.send(new SimpleProcessEvent(this.id(), event.senderId(),
                    ProcessMessages.InstanceCreated.of(createInstance.requestId(), createInstance.processId(), instance.id())));
        }
    }

    protected void enqueue(Event event) {
        if (event.targetId() == null ||
                event.targetId().equals(this.id())) {
            events.add(event);
        }
    }


    @Override
    public ProcessInstance createInstance(Context ctx) {
        var id = SimpleProcessInstanceId.create();
        return register(new ProcessInstanceImpl(id, this, ctx));
    }
}
