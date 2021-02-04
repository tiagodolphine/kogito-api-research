package org.kie.kogito.research.processes.api;

import org.kie.kogito.research.application.api.Context;
import org.kie.kogito.research.application.api.Unit;

public interface Process extends Unit {
    @Override ProcessId id();
    @Override ProcessInstance createInstance(Context ctx);
}
