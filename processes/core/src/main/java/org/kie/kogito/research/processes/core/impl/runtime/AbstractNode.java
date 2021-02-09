package org.kie.kogito.research.processes.core.impl.runtime;

import org.kie.kogito.research.application.api.Event;
import org.kie.kogito.research.application.api.MessageBus;
import org.kie.kogito.research.application.api.SimpleRequestId;
import org.kie.kogito.research.processes.api.NodeEvent;
import org.kie.kogito.research.processes.api.NodeInstanceId;
import org.kie.kogito.research.processes.api.messages.ProcessMessages;
import org.kie.kogito.research.processes.api.runtime.Triggerable;
import org.kie.kogito.research.processes.core.impl.SimpleNodeEvent;
import org.kie.kogito.research.processes.core.impl.messaging.DefaultMailBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractNode implements Triggerable {

    private final NodeInstanceId id;
    private AbstractNode next;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private DefaultMailBox<NodeEvent> mailBox;
    private MessageBus<Event> messageBus;
    private Boolean completed = Boolean.FALSE;

    public AbstractNode() {
        this(null, null, null);
    }

    public AbstractNode(NodeInstanceId id, MessageBus<Event> messageBus, AbstractNode next) {
        this.next = next;
        this.id = id;
        this.messageBus = messageBus;
        mailBox = DefaultMailBox.of(this.messageBus, id);
    }

    public void run() {
        mailBox.events(10).forEach(e -> trigger());
    }

    @Override
    public void trigger() {
        //this should be done via events

        logger.info("Trigger {}", this.getClass().getSimpleName());
        if (next() != null) {
            var triggerInstance = ProcessMessages.TriggerInstance.of(new SimpleRequestId(), null, null, next().id);
            Event event = new SimpleNodeEvent(id(), next.id(), triggerInstance);
            messageBus.send(event);
        }
        completed = Boolean.TRUE;
    }

    //FIXME: for testing should use listeners
    public Boolean completed() {
        return completed;
    }

    public AbstractNode next() {
        return next;
    }

    public NodeInstanceId id() {
        return id;
    }
}