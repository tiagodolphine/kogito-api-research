package org.kie.kogito.research.processes.core.impl;

import org.junit.jupiter.api.Test;
import org.kie.kogito.research.application.api.SimpleRequestId;
import org.kie.kogito.research.application.api.impl.SimpleId;
import org.kie.kogito.research.application.core.impl.BroadcastProcessorMessageBus;
import org.kie.kogito.research.processes.api.messages.ProcessMessages;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProcessMessagingAPITest {

    @Test
    public void createInstance() {
        var messageBus = new BroadcastProcessorMessageBus();
        var process = new ProcessImpl(null, SimpleProcessId.fromString("my.process"), messageBus);
        var createInstance = ProcessMessages.CreateInstance.of(new SimpleRequestId(), SimpleProcessId.fromString("my.process"));
        var selfId = new SimpleId();
        messageBus.send(new SimpleProcessEvent(selfId, null, createInstance));
        messageBus.subscribe(e -> {
            if (e.targetId() == selfId) {
                assertEquals(ProcessMessages.InstanceCreated.class, e.payload().getClass());
                System.out.println(e);
            }
        });
        process.run();
    }
}