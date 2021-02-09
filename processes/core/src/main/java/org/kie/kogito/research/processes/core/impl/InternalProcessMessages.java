package org.kie.kogito.research.processes.core.impl;

import org.kie.kogito.research.application.api.messages.RequestId;
import org.kie.kogito.research.processes.api.ProcessId;
import org.kie.kogito.research.processes.api.ProcessInstanceId;
import org.kie.kogito.research.processes.api.messages.ProcessMessages;
import org.kie.kogito.research.processes.api.messages.ProcessMessages.Request;

public class InternalProcessMessages {

    public static class CompleteProcessInstance extends ProcessMessages.ProcessInstanceMessage implements Request {
        public static CompleteProcessInstance of(RequestId requestId, ProcessId processId, ProcessInstanceId processInstanceId) {
            return new CompleteProcessInstance(requestId, processId, processInstanceId);
        }
        private CompleteProcessInstance(RequestId requestId, ProcessId processId, ProcessInstanceId processInstanceId) {
            super(requestId, processId, processInstanceId);
        }
    }
}
