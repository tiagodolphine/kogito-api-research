package org.kie.kogito.research.processes.core.impl;

import org.kie.kogito.research.application.api.*;
import org.kie.kogito.research.application.api.impl.AbstractUnitContainer;
import org.kie.kogito.research.processes.api.Process;
import org.kie.kogito.research.processes.api.ProcessContainer;
import org.kie.kogito.research.processes.api.ProcessId;

import java.util.Collection;

public class ProcessContainerImpl extends AbstractUnitContainer<Process> implements ProcessContainer {
    public ProcessContainerImpl(Application application) {
        super(application);
    }

    public void register(Collection<? extends Process> processes) {
        for (Process p : processes) {
            super.register(p);
        }
    }

    @Override
    public Process get(UnitId unitId) {
        if (! (unitId instanceof ProcessId)) {
            return null;
        }
        return super.get(unitId);
    }
}
