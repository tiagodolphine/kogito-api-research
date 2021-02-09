package org.kie.kogito.research.processes.core.impl;

import org.kie.kogito.research.application.core.impl.BroadcastProcessorMessageBus;
import org.kie.kogito.research.processes.api.ProcessId;
import org.kie.kogito.research.processes.api.ProcessInstanceId;
import org.kie.kogito.research.processes.api.messages.ProcessMessages;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class BlockingRequestResponse {
    RequestResponse requestResponse;
    private CompletableFuture<ProcessMessages.InstanceCompleted> completed;

    BlockingRequestResponse(BroadcastProcessorMessageBus messageBus) {
        this.requestResponse = new RequestResponse(messageBus);
    }

    ProcessInstanceId createInstance(ProcessId processId) throws ExecutionException, InterruptedException {
        // create instance via message passing
        var createInstance = ProcessMessages.CreateInstance.of(processId);
        var instanceCreated =
                requestResponse.send(createInstance)
                        .expect(ProcessMessages.InstanceCreated.class)
                        .get();
        return instanceCreated.processInstanceId();
    }

    void startInstance(ProcessId processId, ProcessInstanceId processInstanceId) throws ExecutionException, InterruptedException {
        var startInstance =
                ProcessMessages.StartInstance.of(processId, processInstanceId);

        completed = requestResponse.expect(ProcessMessages.InstanceCompleted.class);

        var instanceStarted = requestResponse
                .send(startInstance)
                .expect(ProcessMessages.InstanceStarted.class)
                .get();
    }

    void awaitTermination() throws ExecutionException, InterruptedException {
        completed.get();
    }

}
