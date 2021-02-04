package org.kie.kogito.research.processes.core.impl;

import org.junit.jupiter.api.Test;
import org.kie.kogito.research.application.api.Context;

import java.util.List;

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
}