package org.kie.kogito.research.application.core.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UnitExecutor {

    private ExecutorService services = Executors.newCachedThreadPool();

    public void run(final Runnable unit) {
        Runnable starter = new Runnable() {
            @Override
            public void run() {
                unit.run();
                services.submit(this);
            }
        };
        services.submit(starter);
    }

    public void shutdown() {
        services.shutdown();
    }
}
