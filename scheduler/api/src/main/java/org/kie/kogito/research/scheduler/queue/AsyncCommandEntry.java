package org.kie.kogito.research.scheduler.queue;

import java.util.concurrent.CompletableFuture;

public interface AsyncCommandEntry<T> extends CommandEntry<CompletableFuture<T>> {

    CompletableFuture<T> execute();
}
