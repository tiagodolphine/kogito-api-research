package org.kie.kogito.research.processes.core.impl;

import org.kie.kogito.research.application.api.Context;
import org.kie.kogito.research.application.api.impl.AbstractUnit;
import org.kie.kogito.research.processes.api.Process;
import org.kie.kogito.research.processes.api.ProcessContainer;
import org.kie.kogito.research.processes.api.ProcessId;
import org.kie.kogito.research.processes.api.ProcessInstance;

public class ProcessImpl extends AbstractUnit<ProcessId, ProcessInstance> implements Process {
    public ProcessImpl(ProcessContainer container, ProcessId id) {
        super(container, id);
    }

    @Override
    public ProcessId id() {
        return (ProcessId) super.id();
    }

    @Override
    public ProcessInstance createInstance(Context ctx) {
        var id = SimpleProcessInstanceId.create();
        return register(new ProcessInstanceImpl(id, this, ctx));
    }
}
