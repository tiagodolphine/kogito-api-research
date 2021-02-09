package org.kie.kogito.research.application.core.impl;

import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.MessageBus;

public class GlobalMessageBusHolder {

    private static final MessageBus<Event> MESSAGE_BUS = new BroadcastProcessorMessageBus();

    public static MessageBus<Event> messageBus() {
        return MESSAGE_BUS;
    }
}
