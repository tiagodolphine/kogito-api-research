package org.kie.kogito.research.scheduler.queue;

public interface LinkedCommandEntry<T> extends CommandEntry<T> {

   LinkedCommandEntry<T> next();
   void setNext(LinkedCommandEntry<T> next);
}
