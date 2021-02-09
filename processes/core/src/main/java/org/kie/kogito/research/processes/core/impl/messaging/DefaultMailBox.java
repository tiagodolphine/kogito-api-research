package org.kie.kogito.research.processes.core.impl.messaging;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.Id;
import org.kie.kogito.research.application.api.MessageBus;

public class DefaultMailBox<T extends Event> {

    private final MessageBus<T> messageBus;
    private final Deque<T> events = new ArrayDeque<>();
    private final Id id;

    private DefaultMailBox(MessageBus<T> messageBus, Id id) {
        this.messageBus = messageBus;
        this.id = id;
    }

    public static <T extends Event> DefaultMailBox of(MessageBus<T> messageBus, Id id) {
        return new DefaultMailBox(messageBus, id).init();
    }

    public DefaultMailBox init() {
        messageBus.subscribe(this::enqueue);
        return this;
    }

    protected void enqueue(T event) {
        if (Objects.equals(event.targetId(), id)) {
            events.add(event);
        }
    }

    public Stream<T> events(int batch) {
        //FIXME
        return Optional.ofNullable(events.poll()).stream();
    }
}
