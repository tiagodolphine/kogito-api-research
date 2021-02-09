package org.kie.kogito.research.processes.core.impl;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.Id;
import org.kie.kogito.research.application.api.impl.SimpleId;
import org.kie.kogito.research.application.core.impl.BroadcastProcessorMessageBus;
import org.kie.kogito.research.processes.api.ProcessEvent;
import org.kie.kogito.research.processes.api.messages.ProcessMessages;

import java.util.concurrent.CompletableFuture;

public class RequestResponse {
    private final Id self;
    private final Multi<ProcessMessages.Message> messages;
    private BroadcastProcessor<Event> processor;

    RequestResponse(BroadcastProcessorMessageBus messageBus) {
        this.processor = messageBus.processor();
        this.self = new SimpleId();
        this.messages =
                processor.filter(e -> e.targetId() == self)
                        .map(ProcessEvent.class::cast)
                        .map(ProcessEvent::payload);
    }

    Expect send(ProcessMessages.Message message) {
        return new Expect(message);
    }

    public <E> CompletableFuture<E> expect(Class<E> expectedResponse) {
        return messages
                .filter(expectedResponse::isInstance)
                .map(expectedResponse::cast)
                .toUni().subscribeAsCompletionStage();

    }

    public class Expect {
        private final ProcessMessages.Message message;

        public Expect(ProcessMessages.Message message) {
            this.message = message;
        }

        public <E> CompletableFuture<E> expect(Class<E> expectedResponse) {
            var future =
                    messages
                            .filter(expectedResponse::isInstance)
                            .map(expectedResponse::cast)
                            .toUni().subscribeAsCompletionStage();
            processor.onNext(new SimpleProcessEvent(self, null, message));
            return future;
        }
    }
}
