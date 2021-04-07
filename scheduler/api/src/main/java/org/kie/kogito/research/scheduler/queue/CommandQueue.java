package org.kie.kogito.research.scheduler.queue;

import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

public interface CommandQueue<T extends CommandEntry> {

    int drain(Consumer<T> consumer);

    T poll();

    boolean add(T o);

    int size();

    boolean isEmpty();
}
