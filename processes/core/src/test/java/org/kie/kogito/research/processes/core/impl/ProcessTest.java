package org.kie.kogito.research.processes.core.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.kie.kogito.research.application.api.SimpleRequestId;
import org.kie.kogito.research.application.api.impl.SimpleId;
import org.kie.kogito.research.application.core.impl.BroadcastProcessorMessageBus;
import org.kie.kogito.research.application.core.impl.GlobalMessageBusHolder;
import org.kie.kogito.research.application.core.impl.UnitExecutor;
import org.kie.kogito.research.processes.api.messages.ProcessMessages;
import org.kie.kogito.research.processes.core.impl.runtime.EndNodeInstance;
import org.kie.kogito.research.processes.core.impl.runtime.StartNodeInstance;
import org.kie.kogito.research.processes.core.impl.runtime.TaskInstance;

public class ProcessTest {

    @Test
    public void init() {
        var messageBus = GlobalMessageBusHolder.messageBus();

        // set up the system (internal APIs)
        ExecutorService service = Executors.newCachedThreadPool();
        var processId = SimpleProcessId.fromString("my.process");
        var processContainer = new ProcessContainerImpl(null);
        var process = new ProcessImpl(processContainer, processId, messageBus, service);
        var instance = (ProcessInstanceImpl) process.createInstance(null);

        //TODO: should create the node instances via messaging
        var end = new EndNodeInstance(SimpleNodeInstanceId.create(), messageBus, null);
        var task = new TaskInstance(SimpleNodeInstanceId.create(), messageBus, end);
        var start = new StartNodeInstance(SimpleNodeInstanceId.create(), messageBus, task);
        instance.setStart(start);

        //TODO: running the instances, should be done via messaging
        var unitExecutor = new UnitExecutor();
        //unitExecutor.run(instance::run);
        unitExecutor.run(end::run);
        unitExecutor.run(task::run);
        unitExecutor.run(start::run);

        //TODO: 1create start via message passing, could be done as a method in the ProcessInstance
        var selfId = new SimpleId();
        var createInstance = ProcessMessages.StartInstance.of(process.id(), instance.id());
        instance.messageBus().send(new SimpleProcessEvent(selfId, instance.id(), createInstance));

        //need to wait for all processing since is is executed in different thread other than the main thread
        Awaitility.await().until(() -> end.completed());
    }
}
