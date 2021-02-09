package org.kie.kogito.research.processes.api.messages;

import org.kie.kogito.research.application.api.Context;
import org.kie.kogito.research.application.api.SimpleRequestId;
import org.kie.kogito.research.application.api.messages.RequestId;
import org.kie.kogito.research.processes.api.NodeInstanceId;
import org.kie.kogito.research.processes.api.ProcessId;
import org.kie.kogito.research.processes.api.ProcessInstanceId;
import org.kie.kogito.research.processes.api.SimpleProcessContext;

import java.util.Optional;

public class ProcessMessages {
    // Requests

    public static class CreateInstance extends ProcessMessage implements Request {
        private Context context;

        public static CreateInstance of(ProcessId processId) {
            return new CreateInstance(new SimpleRequestId(), processId, new SimpleProcessContext());
        }

        public static CreateInstance of(ProcessId processId, Context context) {
            return new CreateInstance(new SimpleRequestId(), processId, context);
        }

        public static CreateInstance of(RequestId requestId, ProcessId processId) {
            return new CreateInstance(requestId, processId, new SimpleProcessContext());
        }

        public static CreateInstance of(RequestId requestId, ProcessId processId, Context context) {
            return new CreateInstance(requestId, processId, context);
        }

        private CreateInstance(RequestId requestId, ProcessId processId, Context context) {
            super(requestId, processId);
            this.context = context;
        }

        public Context context() {
            return context;
        }
    }

    public static class StartInstance extends ProcessInstanceMessage implements Request {
        public static StartInstance of(ProcessId processId, ProcessInstanceId processInstanceId) {
            return new StartInstance(new SimpleRequestId(), processId, processInstanceId);
        }

        public static StartInstance of(RequestId requestId, ProcessId processId, ProcessInstanceId processInstanceId) {
            return new StartInstance(requestId, processId, processInstanceId);
        }

        private StartInstance(RequestId requestId, ProcessId processId, ProcessInstanceId processInstanceId) {
            super(requestId, processId, processInstanceId);
        }
    }

    public static class TriggerInstance extends NodeInstanceMessage implements Request{
        public static TriggerInstance of(RequestId requestId, ProcessId processId,
                                         ProcessInstanceId processInstanceId, NodeInstanceId nodeInstanceId) {
            return new TriggerInstance(requestId, processId, processInstanceId, nodeInstanceId);
        }
        private TriggerInstance(RequestId requestId, ProcessId processId, ProcessInstanceId processInstanceId,
                                NodeInstanceId nodeInstanceId) {
            super(requestId, processId, processInstanceId, nodeInstanceId);
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

    public interface Request {
    }

    public interface Response {
    }

    public interface Message {
        <T extends Message> Optional<T> as(Class<T> type);
    }

    public static abstract class ProcessMessage implements Message {
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

        @Override
        public <T extends Message> Optional<T> as(Class<T> type) {
            return type.isInstance(this) ? Optional.of((T) this) : Optional.empty();
        }
    }

    public static abstract class ProcessInstanceMessage extends ProcessMessage {
        private final ProcessInstanceId processInstanceId;

        protected ProcessInstanceMessage(RequestId requestId, ProcessId processId, ProcessInstanceId processInstanceId) {
            super(requestId, processId);
            this.processInstanceId = processInstanceId;
        }

        public ProcessInstanceId processInstanceId() {
            return processInstanceId;
        }
    }

    private static abstract class NodeInstanceMessage extends ProcessInstanceMessage {
        private final NodeInstanceId nodeInstanceId;

        protected NodeInstanceMessage(RequestId requestId, ProcessId processId, ProcessInstanceId processInstanceId,
                                      NodeInstanceId nodeInstanceId) {
            super(requestId, processId, processInstanceId);
            this.nodeInstanceId = nodeInstanceId;
        }
    }
}
