package org.kie.kogito.research.scheduler.queue;

import java.util.function.Consumer;

/**
 * Analogous to PropagationList from DroolsV7
 */
public class LinkedCommandQueueImpl implements LinkedCommandQueue<LinkedCommandEntry> {

    private LinkedCommandEntry head;
    private LinkedCommandEntry tail;

    @Override
    public LinkedCommandEntry drain() {
        LinkedCommandEntry currentHead = head;
        head = null;
        tail = null;
        return currentHead;
    }

    @Override
    public int drain(Consumer<LinkedCommandEntry> consumer) {
        LinkedCommandEntry current;
        int i = 0;
        while ((current = poll()) != null) {
            consumer.accept(current);
            i++;
        }
        return i;
    }

    @Override
    public LinkedCommandEntry poll() {
        if (head == null) {
            return null;
        }
        LinkedCommandEntry entry = head;
        head = head.next();
        entry.setNext(null);
        return entry;
    }

    @Override
    public boolean add(final LinkedCommandEntry entry) {
        if (head == null) {
            head = entry;
        } else {
            tail.setNext(entry);
        }
        tail = entry;
        return true;
    }

    @Override
    public int size() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }
}
