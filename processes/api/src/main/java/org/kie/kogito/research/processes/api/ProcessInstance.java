package org.kie.kogito.research.processes.api;

import org.kie.kogito.research.application.api.MessageBus;
import org.kie.kogito.research.application.api.UnitInstance;

public interface ProcessInstance extends UnitInstance {
    ProcessInstanceId id();
    Process unit();
    MessageBus<ProcessEvent> messageBus();
}
