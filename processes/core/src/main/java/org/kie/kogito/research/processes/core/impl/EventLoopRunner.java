package org.kie.kogito.research.processes.core.impl;

import java.util.concurrent.ExecutorService;

public class EventLoopRunner implements Runnable{

    private final Runnable process;
    private final ExecutorService service;

    public EventLoopRunner(Runnable process, ExecutorService service) {
        this.process = process;
        this.service = service;
    }

    @Override
        public void run() {
            process.run();
            service.submit(this);
        }


}
