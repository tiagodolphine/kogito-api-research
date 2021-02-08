package org.kie.kogito.research.processes.api.messages;

import org.kie.kogito.research.application.api.messages.RequestId;
import org.kie.kogito.research.processes.api.ProcessId;
import org.kie.kogito.research.processes.api.ProcessInstanceId;

public class ProcessMessages {
    // Requests

    public static class CreateInstance extends ProcessMessage implements Request {
        public static CreateInstance of(RequestId requestId, ProcessId processId) {
            return new CreateInstance(requestId, processId);
        }
        private CreateInstance(RequestId requestId, ProcessId processId) {
            super(requestId, processId);
        }
    }
    public static class StartInstance extends ProcessInstanceMessage implements Request{
        public static StartInstance of(RequestId requestId, ProcessId processId, ProcessInstanceId processInstanceId) {
            return new StartInstance(requestId, processId, processInstanceId);
        }
        private StartInstance(RequestId requestId, ProcessId processId, ProcessInstanceId processInstanceId) {
            super(requestId, processId, processInstanceId);
        }
    }

    // Responses

    public static class InstanceCreated extends ProcessInstanceMessage implements Response {
        public static InstanceCreated of(RequestId requestId, ProcessId processId, ProcessInstanceId processInstanceId) {
            return new InstanceCreated(requestId, processId, processInstanceId);
        }
        private InstanceCreated(RequestId requestId, ProcessId processId, ProcessInstanceId processInstanceId) {
            super(requestId, processId, processInstanceId);
        }
    }
    public static class InstanceStarted extends ProcessInstanceMessage implements Response {
        public static InstanceStarted of(RequestId requestId, ProcessId processId, ProcessInstanceId processInstanceId) {
            return new InstanceStarted(requestId, processId, processInstanceId);
        }
        private InstanceStarted(RequestId requestId, ProcessId processId, ProcessInstanceId processInstanceId) {
            super(requestId, processId, processInstanceId);
        }
    }
    public static class InstanceCompleted extends ProcessInstanceMessage implements Response {
        public static InstanceCompleted of(RequestId requestId, ProcessId processId, ProcessInstanceId processInstanceId) {
            return new InstanceCompleted(requestId, processId, processInstanceId);
        }
        private InstanceCompleted(RequestId requestId, ProcessId processId, ProcessInstanceId processInstanceId) {
            super(requestId, processId, processInstanceId);
        }
    }

    interface Request {}
    interface Response {}

    private static abstract class ProcessMessage {
        private final RequestId requestId;
        private final ProcessId processId;

        protected ProcessMessage(RequestId requestId, ProcessId processId) {
            this.requestId = requestId;
            this.processId = processId;
        }

        public RequestId requestId() {
            return requestId;
        }

        public ProcessId processId() {
            return processId;
        }
    }
    private static abstract class ProcessInstanceMessage extends ProcessMessage {
        private final ProcessInstanceId processInstanceId;

        protected ProcessInstanceMessage(RequestId requestId, ProcessId processId, ProcessInstanceId processInstanceId) {
            super(requestId, processId);
            this.processInstanceId = processInstanceId;
        }
    }
}
