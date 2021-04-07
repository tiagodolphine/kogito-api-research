package org.kie.kogito.research.scheduler.queue;

public interface LinkedCommandQueue<T extends LinkedCommandEntry> extends CommandQueue<T> {

    T drain();
}
