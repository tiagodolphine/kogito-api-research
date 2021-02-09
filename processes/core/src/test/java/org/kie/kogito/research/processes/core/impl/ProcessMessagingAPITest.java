package org.kie.kogito.research.processes.core.impl;

import org.junit.jupiter.api.Test;
import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.Id;
import org.kie.kogito.research.application.api.SimpleRequestId;
import org.kie.kogito.research.application.api.impl.SimpleId;
import org.kie.kogito.research.application.core.impl.BroadcastProcessorMessageBus;
import org.kie.kogito.research.processes.api.ProcessInstance;
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

        // create instance via message passing
        var createInstance = ProcessMessages.CreateInstance.of(processId);
        var messages = new RequestResponse(messageBus);
        var instanceCreated =
                messages.send(createInstance)
                        .expect(ProcessMessages.InstanceCreated.class)
                        .get();

        assertEquals(processId, instanceCreated.processId());
        assertEquals(createInstance.requestId(), instanceCreated.requestId());

        var startInstance =
                ProcessMessages.StartInstance.of(processId, instanceCreated.processInstanceId());

        var instanceCompleted = messages
                .send(startInstance)
                .expect(ProcessMessages.InstanceCompleted.class)
                .get();

        assertEquals(processId, instanceCompleted.processId());
        assertEquals(instanceCreated.processInstanceId(), instanceCompleted.processInstanceId());

    }

    private <T> CompletableFuture<T> ask(
            BroadcastProcessorMessageBus messageBus,
            Id selfId,
            ProcessMessages.Message message,
            Class<T> expectedResponse) {
        var instanceCreatedFuture = messageBus.processor()
                .filter(e -> e.targetId() == selfId)
                .map(Event::payload)
                .map(expectedResponse::cast)
                .toUni().subscribeAsCompletionStage();

        messageBus.send(new SimpleProcessEvent(selfId, null, message));
        return instanceCreatedFuture;
    }

}