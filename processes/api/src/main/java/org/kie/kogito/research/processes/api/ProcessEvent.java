package org.kie.kogito.research.processes.api;

import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.processes.api.messages.ProcessMessages;

public interface ProcessEvent extends Event {
    @Override ProcessMessages.Message payload();
}
