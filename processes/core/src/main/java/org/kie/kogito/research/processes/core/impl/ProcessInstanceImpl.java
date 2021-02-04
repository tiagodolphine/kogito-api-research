package org.kie.kogito.research.processes.core.impl;

import org.kie.kogito.research.application.api.Context;
import org.kie.kogito.research.application.api.impl.AbstractUnitInstance;
import org.kie.kogito.research.processes.api.Process;
import org.kie.kogito.research.processes.api.ProcessInstance;
import org.kie.kogito.research.processes.api.ProcessInstanceId;

public class ProcessInstanceImpl extends AbstractUnitInstance implements ProcessInstance {
    public ProcessInstanceImpl(ProcessInstanceId id, Process unit, Context context) {
        super(id, unit, context);
    }

}
