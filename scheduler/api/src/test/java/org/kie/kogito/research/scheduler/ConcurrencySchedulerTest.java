package org.kie.kogito.research.scheduler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;
import org.kie.kogito.research.scheduler.queue.LinkedCommandQueueImpl;
import org.kie.kogito.research.scheduler.queue.RunnableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ConcurrencySchedulerTest {

    private static Logger LOGGER = LoggerFactory.getLogger(ConcurrencySchedulerTest.class);

    @Test
    public void scheduleTest() throws InterruptedException {
        int total = 1000 + 3;//total number of commands to be added
        Scheduler scheduler = Scheduler.get(new LinkedCommandQueueImpl()).init();
        //Scheduler scheduler = Scheduler.get(new DelegateCommandQueue()).init();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(total);

        StopWatch watch = new StopWatch();
        watch.start();

        //adding command in the main thread
        scheduler.add(RunnableCommand.of(() -> {
            LOGGER.debug("Executing - START");
            latch.countDown();
            return null;
        }));

        //Test the Timer Thread to insert a command in the queue
        scheduler.schedule(RunnableCommand.of(() -> {
            LOGGER.debug("Executing - SCHEDULED");
            latch.countDown();
            return null;
        }), 1, TimeUnit.MILLISECONDS);

        //adding commands in different threads to simulate the user threads
        IntStream.range(0, (total - 2)).forEach(i -> CompletableFuture.runAsync(
                () -> scheduler.add(RunnableCommand.of(() -> {
                    LOGGER.debug("Executing - {}", i);
                    latch.countDown();
                    return null;
                })), executor));

        //adding command in the main thread
        scheduler.add(RunnableCommand.of(() -> {
            LOGGER.debug("Executing - END");
            latch.countDown();
            return null;
        }));

        latch.await();
        watch.stop();
        LOGGER.info("Elapsed Time Ms {}", watch.getTime());
    }
}