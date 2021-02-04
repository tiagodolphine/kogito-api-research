package org.kie.kogito.research.application.api.impl;

import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.MessageBus;

import java.util.function.Consumer;

public class LambdaMessageBus<T extends Event> implements MessageBus<T> {
    private final Consumer<T> consumer;

    public LambdaMessageBus(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void send(T event) {
        consumer.accept(event);
    }

    @Override
    public void subscribe(Consumer<T> consumer) {
        //no op
    }


}
