package org.kie.kogito.research.scheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.kie.kogito.research.scheduler.queue.AsyncCommandEntry;
import org.kie.kogito.research.scheduler.queue.CommandEntry;
import org.kie.kogito.research.scheduler.queue.CommandQueue;
import org.kie.kogito.research.scheduler.queue.DelegateCommandQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Scheduler {

    private static Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);
    private final AtomicBoolean active = new AtomicBoolean(false);
    private final Map<Class, CommandHandler> handlers = new HashMap<>();
    private final CommandQueue<? super CommandEntry> queue;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition wake = lock.newCondition();
    private TimerService timerService = new TimerService();
    private Thread engineThread;

    private Scheduler(CommandQueue queue) {
        this.queue = queue;
    }

    public static Scheduler get() {
        return new Scheduler(new DelegateCommandQueue());
    }

    public static Scheduler get(CommandQueue queue) {
        return new Scheduler(queue);
    }

    public Scheduler init() {
        active.set(true);
        engineThread = new Thread(this::loop);
        engineThread.setName("EngineThread");
        engineThread.start();
        return this;
    }

    private void loop() {
        while (active.get()) {
            try {
                lock.lock();
                int drained = queue.drain(entry -> {
                    if (entry == null) {
                        return;
                    }
                    if (entry instanceof CommandEntry) {
                        entry.execute();
                    }
                    if (entry instanceof AsyncCommandEntry) {
                        //todo
                    }

                    //todo: allow callback handlers per type?
                    //handlers.get(entry.getClass()).handle(entry);
                });
                LOGGER.debug("Drained entries = {}", drained);
                rest();
            } finally {
                lock.unlock();
            }
        }
    }

    public void add(CommandEntry command) {
        //todo: should it be moved to the queue impl?
        try {
            LOGGER.debug("Add");
            lock.lock();
            queue.add(command);
            wake.signal();
        } finally {
            lock.unlock();
        }
    }

    private void rest() {
        try {
            lock.lock();
            if (!queue.isEmpty()) {
                return;
            }
            wake.awaitUninterruptibly();
        } finally {
            lock.unlock();
        }
    }

    public void schedule(CommandEntry command, long delay, TimeUnit timeUnit) {
        timerService.schedule(() -> add(command), delay, timeUnit);
    }
}