package org.kie.kogito.research.scheduler.queue;

import java.util.function.Supplier;

public class RunnableCommand implements CommandEntry<Void>,
                                        LinkedCommandEntry<Void> {

    private Runnable command;
    private LinkedCommandEntry next;

    private RunnableCommand(Runnable command) {
        this.command = command;
    }

    public static RunnableCommand of(Runnable command) {
        return new RunnableCommand(command);
    }

    public static RunnableCommand of(Supplier command) {
        return new RunnableCommand(command::get);
    }

    @Override
    public LinkedCommandEntry next() {
        return next;
    }

    @Override
    public void setNext(LinkedCommandEntry next) {
        this.next = next;
    }

    @Override
    public Void execute() {
        command.run();
        return null;
    }
}
