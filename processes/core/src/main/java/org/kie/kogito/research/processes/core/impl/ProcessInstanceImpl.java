package org.kie.kogito.research.processes.core.impl;

import org.kie.kogito.research.application.api.Context;
import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.ExecutionModel;
import org.kie.kogito.research.application.api.MessageBus;
import org.kie.kogito.research.application.api.impl.AbstractUnitInstance;
import org.kie.kogito.research.processes.api.Process;
import org.kie.kogito.research.processes.api.ProcessEvent;
import org.kie.kogito.research.processes.api.ProcessInstance;
import org.kie.kogito.research.processes.api.ProcessInstanceId;

import java.util.ArrayDeque;
import java.util.Deque;

public class ProcessInstanceImpl extends AbstractUnitInstance implements ProcessInstance {
    private final ExecutionModel executionModel;
    private final Deque<Event> events;

    public ProcessInstanceImpl(ProcessInstanceId id, ProcessImpl unit, Context context) {
        super(id, unit, context);
        if (context instanceof ExecutionModel) {
            this.executionModel = (ExecutionModel) context;
            this.events = new ArrayDeque<>();
            unit().messageBus().subscribe(this::enqueue);
        } else {
            this.executionModel = null;
            this.events = null;
        }
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
            executionModel.onEvent(event);
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
