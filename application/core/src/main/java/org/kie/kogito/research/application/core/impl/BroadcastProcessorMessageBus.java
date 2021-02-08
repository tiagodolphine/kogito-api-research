package org.kie.kogito.research.application.core.impl;

import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.MessageBus;

import java.util.function.Consumer;

public class BroadcastProcessorMessageBus implements MessageBus<Event> {
    BroadcastProcessor<Event> processor = BroadcastProcessor.create();

    public BroadcastProcessor<Event> processor() {
        return processor;
    }

    @Override
    public void send(Event event) {
        processor.onNext(event);
    }

    @Override
    public void subscribe(Consumer<Event> consumer) {
        processor.subscribe().with(consumer);
    }
}