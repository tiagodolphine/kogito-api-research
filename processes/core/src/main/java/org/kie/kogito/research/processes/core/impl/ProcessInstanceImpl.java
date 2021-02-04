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

public class ProcessInstanceImpl extends AbstractUnitInstance implements ProcessInstance {
    private final ExecutionModel executionModel;

    public ProcessInstanceImpl(ProcessInstanceId id, Process unit, Context context) {
        super(id, unit, context);
        if (context instanceof ExecutionModel) {
            this.executionModel = (ExecutionModel) context;
            unit().messageBus().subscribe(this::receive);
        } else {
            this.executionModel = null;
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

    protected void receive(Event event) {
        if (executionModel == null) {
            return;
        }
        if (event.targetId() == null ||
                event.targetId().equals(this.unit().id()) ||
                event.targetId().equals(this.id())) {
            executionModel.onEvent(event);
        }
    }

}
