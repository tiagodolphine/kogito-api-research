package org.kie.kogito.research.scheduler;

public interface CommandHandler<T> {

    Class<T> type();
    CommandHandler handle(T data);

}
