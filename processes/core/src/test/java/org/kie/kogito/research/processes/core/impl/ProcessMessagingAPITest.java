package org.kie.kogito.research.processes.core.impl;

import org.junit.jupiter.api.Test;
import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.Id;
import org.kie.kogito.research.application.api.SimpleRequestId;
import org.kie.kogito.research.application.api.impl.SimpleId;
import org.kie.kogito.research.application.core.impl.BroadcastProcessorMessageBus;
import org.kie.kogito.research.processes.api.ProcessInstance;
import org.kie.kogito.research.processes.api.ProcessInstanceId;
import org.kie.kogito.research.processes.api.messages.ProcessMessages;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProcessMessagingAPITest {

    @Test
    public void createInstance() throws InterruptedException, ExecutionException {
        // set up the system (internal APIs)
        ExecutorService service = Executors.newCachedThreadPool();
        var messageBus = new BroadcastProcessorMessageBus();
        var processId = SimpleProcessId.fromString("my.process");
        var process = new ProcessImpl(null, processId, messageBus, service);

        // a test utility that wraps the bus to await responses
        var messages = new RequestResponse(messageBus);

        // create instance via message passing
        var createInstance = ProcessMessages.CreateInstance.of(processId);
        var instanceCreated =
                messages.send(createInstance)
                        .expect(ProcessMessages.InstanceCreated.class)
                        .get();

        assertEquals(createInstance.requestId(), instanceCreated.requestId());
        assertEquals(processId, instanceCreated.processId());

        // listen for process completion
        var instanceCompletedListener =
                messages.expect(ProcessMessages.InstanceCompleted.class);

        var processInstanceId = instanceCreated.processInstanceId();

        // start instance
        var startInstance =
                ProcessMessages.StartInstance.of(processId, processInstanceId);
        var instanceStarted =
                messages.send(startInstance)
                        .expect(ProcessMessages.InstanceStarted.class).get();

        assertEquals(startInstance.requestId(), instanceStarted.requestId());
        assertEquals(processId, instanceStarted.processId());
        assertEquals(processInstanceId, instanceStarted.processInstanceId());

        var instanceCompleted = instanceCompletedListener.get();

        assertEquals(processId, instanceCompleted.processId());
        assertEquals(processInstanceId, instanceCompleted.processInstanceId());

    }

    @Test
    public void blockingCreateInstance() throws ExecutionException, InterruptedException {
        // set up the system (internal APIs)
        ExecutorService service = Executors.newCachedThreadPool();
        var messageBus = new BroadcastProcessorMessageBus();
        var processId = SimpleProcessId.fromString("my.process");
        var process = new ProcessImpl(null, processId, messageBus, service);

        // a test utility that wraps the bus to await responses
        var api = new BlockingRequestResponse(messageBus);
        var instanceId = api.createInstance(processId);
        api.startInstance(processId, instanceId);
        api.awaitTermination();
    }

}