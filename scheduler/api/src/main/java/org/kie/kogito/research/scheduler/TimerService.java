package org.kie.kogito.research.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerService {

    private final ScheduledExecutorService scheduler;

    public TimerService() {
        scheduler = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "TimerThread"));
    }

    public void schedule(Runnable command, long delay, TimeUnit timeUnit) {
        scheduler.schedule(command, delay, timeUnit);
    }
}
