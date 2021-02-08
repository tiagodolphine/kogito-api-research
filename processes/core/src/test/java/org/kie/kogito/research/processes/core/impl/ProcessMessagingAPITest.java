package org.kie.kogito.research.processes.core.impl;

import org.junit.jupiter.api.Test;
import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.SimpleRequestId;
import org.kie.kogito.research.application.api.impl.SimpleId;
import org.kie.kogito.research.application.core.impl.BroadcastProcessorMessageBus;
import org.kie.kogito.research.processes.api.messages.ProcessMessages;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProcessMessagingAPITest {

    @Test
    public void createInstance() throws InterruptedException, ExecutionException {
        // set up the system (internal APIs)
        var messageBus = new BroadcastProcessorMessageBus();
        var processId = SimpleProcessId.fromString("my.process");
        var process = new ProcessImpl(null, processId, messageBus);

        // create instance via message passing
        var selfId = new SimpleId();
        var requestId = new SimpleRequestId();
        var createInstance = ProcessMessages.CreateInstance.of(requestId, processId);
        messageBus.send(new SimpleProcessEvent(selfId, null, createInstance));

        // await confirmation
        var instanceCreatedFuture = messageBus.processor()
                .filter(e -> e.targetId() == selfId)
                .map(Event::payload)
                .map(ProcessMessages.InstanceCreated.class::cast)
                .toUni().subscribeAsCompletionStage();
        process.run();
        var instanceCreated = instanceCreatedFuture.get();
        assertEquals(processId, instanceCreated.processId());
        assertEquals(requestId, instanceCreated.requestId());
    }
}