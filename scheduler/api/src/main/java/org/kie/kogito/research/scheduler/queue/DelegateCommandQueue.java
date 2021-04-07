package org.kie.kogito.research.scheduler.queue;

import java.util.function.Consumer;

import org.jctools.queues.MpscArrayQueue;

/**
 * Delegate to JCTools
 */
public class DelegateCommandQueue implements CommandQueue<CommandEntry> {

    public static final int DEFAULT_CAPACITY = 100000000;
    private final MpscArrayQueue<CommandEntry> queue;

    public DelegateCommandQueue(int capacity) {
        queue = new MpscArrayQueue<>(capacity);
    }

    public DelegateCommandQueue() {
        queue = new MpscArrayQueue<>(DEFAULT_CAPACITY);
    }

    @Override
    public int drain(Consumer<CommandEntry> consumer) {
        return queue.drain(consumer::accept);
    }

    @Override
    public boolean add(CommandEntry o) {
        return queue.add(o);
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public CommandEntry poll() {
        return queue.poll();
    }
}
