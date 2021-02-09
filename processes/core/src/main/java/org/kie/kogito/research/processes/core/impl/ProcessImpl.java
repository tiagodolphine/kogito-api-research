package org.kie.kogito.research.processes.core.impl;

import org.kie.kogito.research.application.api.*;
import org.kie.kogito.research.application.api.impl.AbstractUnit;
import org.kie.kogito.research.application.api.impl.LambdaMessageBus;
import org.kie.kogito.research.application.api.messages.RequestId;
import org.kie.kogito.research.processes.api.*;
import org.kie.kogito.research.processes.api.Process;
import org.kie.kogito.research.processes.api.messages.ProcessMessages;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;

public class ProcessImpl extends AbstractUnit<ProcessId, ProcessInstance> implements Process {
    private final MessageBus<ProcessEvent> messageBus;
    private ExecutorService service;
    private final Deque<Event> events;

    public ProcessImpl(ProcessContainer container, ProcessId id) {
        super(container, id);
        this.messageBus = new LambdaMessageBus<>(this::send);
        this.events = null;
    }

    public ProcessImpl(
            ProcessContainerImpl processContainer,
            SimpleProcessId id,
            MessageBus<? extends Event> messageBus) {
        super(processContainer, id);
        this.messageBus = (MessageBus<ProcessEvent>) messageBus;
        this.events = new ConcurrentLinkedDeque<>();
        messageBus.subscribe(this::enqueue);
    }


    public ProcessImpl(
            ProcessContainerImpl processContainer,
            SimpleProcessId id,
            MessageBus<? extends Event> messageBus,
            ExecutorService service) {
        this(processContainer, id, messageBus);
        this.service = service;
        service.submit(new EventLoopRunner(this::run, service));
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
        if (event instanceof ProcessEvent) {
            ProcessEvent pEvent = (ProcessEvent) event;
            pEvent.payload().as(ProcessMessages.CreateInstance.class).ifPresent(e ->
                createInstance0(e.requestId(), event.senderId(), new SimpleProcessContext()));
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
        return createInstance0(new SimpleRequestId(), id(), ctx);
    }

    protected ProcessInstance createInstance0(RequestId requestId, Id senderId, Context ctx) {
        var id = SimpleProcessInstanceId.create();
        return register(new ProcessInstanceImpl(requestId, senderId, id,this, ctx, service));
    }
}
