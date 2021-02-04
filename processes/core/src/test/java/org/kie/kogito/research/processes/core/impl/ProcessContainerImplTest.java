package org.kie.kogito.research.processes.core.impl;

import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import org.junit.jupiter.api.Test;
import org.kie.kogito.research.application.api.Context;
import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.ExecutionModel;
import org.kie.kogito.research.application.api.MessageBus;
import org.kie.kogito.research.processes.api.ProcessEvent;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProcessContainerImplTest {

    @Test
    public void lookup() {
        var processContainer = new ProcessContainerImpl(null);
        var aProcess = new ProcessImpl(processContainer, SimpleProcessId.fromString("a.process"));
        var anotherProcess = new ProcessImpl(processContainer, SimpleProcessId.fromString("another.process"));
        var thirdProcess = new ProcessImpl(processContainer, SimpleProcessId.fromString("third.process"));
        processContainer.register(List.of(
                aProcess,
                anotherProcess,
                thirdProcess
        ));


        class MyProcessVariables implements Context {
            String name;
        }
        var p = processContainer.get(SimpleProcessId.fromString("third.process"));
        assertEquals(thirdProcess, p);
        var instance = p.createInstance(new MyProcessVariables());
    }

    static class MyMessageBus implements MessageBus<ProcessEvent> {
        BroadcastProcessor<ProcessEvent> processor = BroadcastProcessor.create();

        @Override
        public void send(ProcessEvent event) {
            processor.onNext(event);
        }

        @Override
        public void subscribe(Consumer<ProcessEvent> consumer) {
            processor.subscribe().with(consumer);
        }
    }

    @Test
    public void messaging() {
        var messageBus = new MyMessageBus();
        var processContainer = new ProcessContainerImpl(null, messageBus);
        var aProcess = new ProcessImpl(processContainer, SimpleProcessId.fromString("a.process"), messageBus);
        var anotherProcess = new ProcessImpl(processContainer, SimpleProcessId.fromString("another.process"), messageBus);
        var thirdProcess = new ProcessImpl(processContainer, SimpleProcessId.fromString("third.process"), messageBus);
        processContainer.register(List.of(
                aProcess,
                anotherProcess,
                thirdProcess));

        class MyProcessVariables implements Context, ExecutionModel {
            String name;

            @Override
            public void onEvent(Event event) {
                System.out.println(name + ": Got event " + event);
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

        messageBus.send(
                new SimpleProcessEvent(null, SimpleProcessId.fromString("a.process"), "Payload"));


    }
}