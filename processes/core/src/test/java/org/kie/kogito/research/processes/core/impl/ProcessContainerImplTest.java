package org.kie.kogito.research.processes.core.impl;

import org.junit.jupiter.api.Test;
import org.kie.kogito.research.application.api.Context;
import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.ExecutionModel;
import org.kie.kogito.research.application.core.impl.BroadcastProcessorMessageBus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProcessContainerImplTest {

    @Test
    public void lookup() {
        // internal APIs
        var processContainer = new ProcessContainerImpl(null);
        var aProcess = new ProcessImpl(processContainer, SimpleProcessId.fromString("a.process"));
        var anotherProcess = new ProcessImpl(processContainer, SimpleProcessId.fromString("another.process"));
        var thirdProcess = new ProcessImpl(processContainer, SimpleProcessId.fromString("third.process"));

        processContainer.register(List.of(
                aProcess,
                anotherProcess,
                thirdProcess));
        // end of internal APIs

        class MyProcessVariables implements Context {
            String name;
        }
        var p = processContainer.get(SimpleProcessId.fromString("third.process"));
        assertEquals(thirdProcess, p);
        var instance = p.createInstance(new MyProcessVariables());
    }

    @Test
    public void messaging() {
        // internal
        var messageBus = new BroadcastProcessorMessageBus();
        var processContainer = new ProcessContainerImpl(null, messageBus);
        var aProcess = new ProcessImpl(processContainer, SimpleProcessId.fromString("a.process"), messageBus);
        var anotherProcess = new ProcessImpl(processContainer, SimpleProcessId.fromString("another.process"), messageBus);
        var thirdProcess = new ProcessImpl(processContainer, SimpleProcessId.fromString("third.process"), messageBus);
        processContainer.register(List.of(
                aProcess,
                anotherProcess,
                thirdProcess));
        // end of internal

        class MyProcessVariables implements Context, ExecutionModel {
            String name;
            Event event;

            @Override
            public void onEvent(Event event) {
                System.out.println(name + ": Got event " + event);
                this.event = event;
            }
        }

        var ctx1 = new MyProcessVariables();
        ctx1.name = "a";
        var a = processContainer.get(SimpleProcessId.fromString("a.process"))
                .createInstance(ctx1);

        var ctx2 = new MyProcessVariables();
        ctx2.name = "third";
        var third =
                processContainer.get(SimpleProcessId.fromString("third.process"))
                        .createInstance(ctx2);


        var event = new SimpleProcessEvent(null, SimpleProcessId.fromString("a.process"), "Payload");
        a.run();
        third.run();

        assertNull(ctx1.event);
        assertNull(ctx2.event);

        messageBus.send(event);
        // send is async

        assertNull(ctx1.event);
        assertNull(ctx2.event);

        // simulate scheduler, dequeue messages
        a.run();
        third.run();

        assertEquals(event, ctx1.event);
        assertNull(ctx2.event);
    }
}