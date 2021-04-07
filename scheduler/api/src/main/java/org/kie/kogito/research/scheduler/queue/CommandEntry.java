package org.kie.kogito.research.scheduler.queue;

public interface CommandEntry<T> {

    T execute();

}
