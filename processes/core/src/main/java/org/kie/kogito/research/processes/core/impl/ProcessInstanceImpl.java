package org.kie.kogito.research.processes.core.impl;

import java.util.Deque;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;

import org.kie.kogito.research.application.api.Context;
import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.ExecutionModel;
import org.kie.kogito.research.application.api.Id;
import org.kie.kogito.research.application.api.MessageBus;
import org.kie.kogito.research.application.api.SimpleRequestId;
import org.kie.kogito.research.application.api.impl.AbstractUnitInstance;
import org.kie.kogito.research.application.api.messages.RequestId;
import org.kie.kogito.research.application.core.impl.GlobalMessageBusHolder;
import org.kie.kogito.research.processes.api.Process;
import org.kie.kogito.research.processes.api.ProcessEvent;
import org.kie.kogito.research.processes.api.ProcessInstance;
import org.kie.kogito.research.processes.api.ProcessInstanceId;
import org.kie.kogito.research.processes.api.messages.ProcessMessages;
import org.kie.kogito.research.processes.api.runtime.Triggerable;
import org.kie.kogito.research.processes.core.impl.runtime.StartNodeInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessInstanceImpl extends AbstractUnitInstance implements ProcessInstance,
                                                                         Triggerable {

    private final Optional<ExecutionModel> executionModel;
    private final Deque<Event> events;
    private StartNodeInstance start;
    private MessageBus<Event> messageBus = GlobalMessageBusHolder.messageBus();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Id senderId;

    public ProcessInstanceImpl(RequestId requestId, Id senderId, ProcessInstanceId id, ProcessImpl unit, Context context, ExecutorService service) {
        super(id, unit, context);
        this.events = new ConcurrentLinkedDeque<>();
        messageBus().subscribe(this::enqueue);
        messageBus().send(new SimpleProcessEvent(this.id(), senderId,
                ProcessMessages.InstanceCreated.of(requestId, unit.id(), id)));

        this.senderId = senderId;

        executionModel = Optional.ofNullable(context)
                .filter(ExecutionModel.class::isInstance)
                .map(ExecutionModel.class::cast);

        service.submit(new EventLoopRunner(this::run, service));
    }

    @Override
    public ProcessInstanceId id() {
        return (ProcessInstanceId) super.id();
    }

    @Override
    public Process unit() {
        return (Process) super.unit();
    }

    @Override
    public MessageBus<Event> messageBus() {
//        return (MessageBus<ProcessEvent>) unit().messageBus();
        return messageBus;
    }

    public void run() {
        for (Event event = events.poll(); event != null; event = events.poll()) {
            receive(event);
        }
    }

    protected void receive(Event event) {
        executionModel.ifPresent(e -> e.onEvent(event));

        // internal handling logic
        if (event instanceof ProcessEvent) {
            ProcessEvent pEvent = (ProcessEvent) event;
            pEvent.payload().as(ProcessMessages.StartInstance.class)
                    .ifPresent(e -> {
                        messageBus().send(new SimpleProcessEvent(this.id(), pEvent.senderId(),
                                ProcessMessages.InstanceStarted.of(e.requestId(), unit().id(), id())));
                        messageBus().send(new SimpleProcessEvent(this.id(), this.id(),
                                InternalProcessMessages.CompleteProcessInstance.of(new SimpleRequestId(), unit().id(), id())));

                        //trigger next node
                        trigger();
                    });
            pEvent.payload().as(InternalProcessMessages.CompleteProcessInstance.class)
                    .filter(e -> e.processInstanceId().equals(this.id()))
                    .ifPresent(e ->
                            messageBus().send(new SimpleProcessEvent(this.id(), senderId,
                                    ProcessMessages.InstanceCompleted.of(new SimpleRequestId(), unit().id(), id()))));
        }
    }

    protected void enqueue(Event event) {
        if (event.targetId() == null ||
                event.targetId().equals(this.unit().id()) ||
                event.targetId().equals(this.id())) {
            events.add(event);
        }
    }

    @Override
    public void trigger() {
        logger.info("Trigger Process Instance {}", id());
        if(start == null){
            return;
        }
        var triggerInstance = ProcessMessages.TriggerInstance.of(new SimpleRequestId(), this.unit().id(), id(), start.id());
        Event event = new SimpleNodeEvent(id(), start.id(), triggerInstance);
        messageBus.send(event);
    }

    public void setStart(StartNodeInstance start) {
        this.start = start;
    }
}
