package org.kie.kogito.research.processes.core.impl;

import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.Id;
import org.kie.kogito.research.application.api.impl.SimpleId;
import org.kie.kogito.research.application.core.impl.BroadcastProcessorMessageBus;
import org.kie.kogito.research.processes.api.messages.ProcessMessages;

import java.util.concurrent.CompletableFuture;

public class RequestResponse {
    private final Id self;
    private final BroadcastProcessorMessageBus messageBus;

    RequestResponse(BroadcastProcessorMessageBus messageBus) {
        this.messageBus = messageBus;
        this.self = new SimpleId();
    }

    Expect send(ProcessMessages.Message message) {
        return new Expect(message);
    }

    public class Expect {
        private final ProcessMessages.Message message;

        public Expect(ProcessMessages.Message message) {
            this.message = message;
        }

        public <E> CompletableFuture<E> expect(Class<E> expectedResponse) {
            var future = messageBus.processor()
                    .filter(e -> e.targetId() == self)
                    .map(Event::payload)
                    .map(expectedResponse::cast)
                    .toUni().subscribeAsCompletionStage();
            messageBus.send(new SimpleProcessEvent(self, null, message));
            return future;
        }
    }
}
