package org.kie.kogito.research.processes.core.impl;

import org.kie.kogito.research.application.api.*;
import org.kie.kogito.research.application.api.impl.AbstractUnitInstance;
import org.kie.kogito.research.processes.api.Process;
import org.kie.kogito.research.processes.api.ProcessEvent;
import org.kie.kogito.research.processes.api.ProcessInstance;
import org.kie.kogito.research.processes.api.ProcessInstanceId;

public class ProcessInstanceImpl extends AbstractUnitInstance implements ProcessInstance {
    public ProcessInstanceImpl(ProcessInstanceId id, Process unit, Context context) {
        super(id, unit, context);
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
        return this::send;
    }

    protected void send(ProcessEvent event) {
        if (context() instanceof ExecutionModel) {
            ExecutionModel executionModel = (ExecutionModel) this.context();
            if (event.targetId() == null ||
                    event.targetId().equals(this.unit().id()) ||
                    event.targetId().equals(this.id())) {
                executionModel.onEvent(event);
            }
        }
    }

}
