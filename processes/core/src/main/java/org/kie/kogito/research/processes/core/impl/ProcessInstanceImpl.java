package org.kie.kogito.research.processes.core.impl;

import org.kie.kogito.research.application.api.*;
import org.kie.kogito.research.application.api.impl.AbstractUnitInstance;
import org.kie.kogito.research.application.api.messages.RequestId;
import org.kie.kogito.research.processes.api.*;
import org.kie.kogito.research.processes.api.Process;
import org.kie.kogito.research.processes.api.messages.ProcessMessages;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;

public class ProcessInstanceImpl extends AbstractUnitInstance implements ProcessInstance {
    private final ExecutionModel executionModel;
    private final Deque<Event> events;
    private final Id senderId;

    public ProcessInstanceImpl(RequestId requestId, Id senderId, ProcessInstanceId id, ProcessImpl unit, Context context, ExecutorService service) {
        super(id, unit, context);
        this.events = new ConcurrentLinkedDeque<>();
        messageBus().subscribe(this::enqueue);
        messageBus().send(new SimpleProcessEvent(this.id(), senderId,
                ProcessMessages.InstanceCreated.of(requestId, unit.id(), id)));

        this.senderId = senderId;

        if (context instanceof ExecutionModel) {
            this.executionModel = (ExecutionModel) context;
        } else {
            this.executionModel = null;
        }

        service.submit(new EventLoopRunner(this::run, service));
    }

    @Override
    public ProcessInstanceId id() {
        return (ProcessInstanceId) super.id();
    }

    @Override
    public Process unit() {
        return (Process) super.unit();
    }

    @Override
    public MessageBus<ProcessEvent> messageBus() {
        return (MessageBus<ProcessEvent>) unit().messageBus();
    }

    public void run() {
        for (Event event = events.poll(); event != null; event = events.poll()) {
            receive(event);
        }
    }

    protected void receive(Event event) {
        if (executionModel != null) executionModel.onEvent(event);

        // internal handling logic
        if (event instanceof ProcessEvent) {
            ProcessEvent pEvent = (ProcessEvent) event;
            pEvent.payload().as(ProcessMessages.StartInstance.class)
                    .ifPresent(e ->
                            messageBus().send(new SimpleProcessEvent(this.id(), this.id(),
                                    InternalProcessMessages.CompleteProcessInstance.of(new SimpleRequestId(), unit().id(), id()))));
            pEvent.payload().as(InternalProcessMessages.CompleteProcessInstance.class)
                    .filter(e -> e.processInstanceId().equals(this.id()))
                    .ifPresent(e ->
                            messageBus().send(new SimpleProcessEvent(this.id(), senderId,
                                    ProcessMessages.InstanceCompleted.of(new SimpleRequestId(), unit().id(), id()))));
        }
    }

    protected void enqueue(Event event) {
        if (event.targetId() == null ||
                event.targetId().equals(this.unit().id()) ||
                event.targetId().equals(this.id())) {
            events.add(event);
        }
    }

}
