package org.kie.kogito.research.application.api.impl;

import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.MessageBus;

import java.util.Deque;
import java.util.function.Consumer;

public class DelayedLambdaMessageBus<T extends Event> implements MessageBus<T> {
    private final Consumer<T> consumer;
    private final Deque<T> deque;

    public DelayedLambdaMessageBus(Deque<T> deque, Consumer<T> consumer) {
        this.deque = deque;
        this.consumer = consumer;
    }

    @Override
    public void send(T event) {
        this.deque.add(event);
    }
}
