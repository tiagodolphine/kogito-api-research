package org.kie.kogito.research.application.api;

import java.util.ArrayDeque;
import java.util.Deque;

import org.junit.jupiter.api.Test;
import org.kie.kogito.research.application.api.impl.AbstractApplication;
import org.kie.kogito.research.application.api.impl.AbstractUnit;
import org.kie.kogito.research.application.api.impl.AbstractUnitContainer;
import org.kie.kogito.research.application.api.impl.AbstractUnitInstance;
import org.kie.kogito.research.application.api.impl.SimpleEvent;
import org.kie.kogito.research.application.api.impl.SimpleUnitId;
import org.kie.kogito.research.application.api.impl.SimpleUnitInstanceId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessagingTest {

    @Test
    public void test() {
        var app = new MyApplication();

        ContextA ctx1 = new ContextA();
        var a = app.get(MyUnitContainer.class)
                        .get(MyUnit.ID)
                        .createInstance(ctx1);

        ContextA ctx2 = new ContextA();
        var b = app.get(AnotherUnitContainer.class)
                .get(ClassUnitId.of(AnotherUnit.class))
                .createInstance(ctx2);

        ContextB ctx3 = new ContextB();
        var c = app.get(AnotherUnitContainer.class)
                .get(ClassUnitId.of(AnotherUnit.class))
                .createInstance(ctx3);

        assertNull(ctx1.message);
        assertNull(ctx2.message);
        assertFalse(ctx3.gotMessage);

        app.send(SimpleEvent.of(null, ClassUnitId.of(AnotherUnit.class), "SPECIAL"));

        assertNull(ctx1.message);
        assertNull(ctx2.message);
        assertFalse(ctx3.gotMessage);

        app.run();

        assertNotNull(ctx1.message);
        System.out.println(ctx1.message);
        assertEquals("SPECIAL", ctx2.message);
        assertTrue(ctx3.gotMessage);


    }
    static class ContextA implements Context, ExecutionModel {
        String message;
        @Override
        public void onEvent(Event event) {
            message = (String) event.payload();
        }
    }

    static class ContextB implements Context, ExecutionModel {

        public boolean gotMessage = false;

        @Override
        public void onEvent(Event event) {
            gotMessage = true;
        }
    }

    static class MyApplication extends AbstractApplication {
        Deque<Event> deque = new ArrayDeque<>();
        MyApplication() {
            register(new MyUnitContainer(this));
            register(new AnotherUnitContainer(this));
        }

        @Override
        public void send(Event event) {
            deque.offer(event);
        }

        public void run() {
            Event evt = deque.poll();
            while (evt != null) {
                super.send(evt);
                evt = deque.poll();
            }

        }
    }
    static class MyUnitContainer extends AbstractUnitContainer<MyUnit> {
        MyUnitContainer(Application app) {
            super(app);
            register(new MyUnit(this));
        }

    }
    static class MyUnit extends AbstractUnit<UnitId, MyUnitInstance> {
        public static final UnitId ID = new SimpleUnitId();
        public MyUnit(MyUnitContainer container) {
            super(container, ID);
        }
        public MyUnitInstance createInstance(Context ctx) {
            return register(new MyUnitInstance(this, ctx));
        }

    }
    static class MyUnitInstance extends AbstractUnitInstance {
        public MyUnitInstance(MyUnit myUnit, Context context) {
            super(new SimpleUnitInstanceId(), myUnit, context);
        }

        @Override
        public void run() {

        }
    }


    static class AnotherUnitContainer extends AbstractUnitContainer<AnotherUnit> {
        AnotherUnitContainer(Application app) {
            super(app);
            register(new AnotherUnit(this));
        }
    }

    static class AnotherUnit extends AbstractUnit<UnitId, AnotherUnitInstance> {
        public static final UnitId ID = ClassUnitId.of(AnotherUnit.class);

        public AnotherUnit(AnotherUnitContainer container) {
            super(container, ID);
        }
        public AnotherUnitInstance createInstance(Context ctx) {
            return register(new AnotherUnitInstance(this, ctx));
        }
    }

    static class AnotherUnitInstance extends AbstractUnitInstance {
        public AnotherUnitInstance(AnotherUnit myUnit, Context ctx) {
            super(new SimpleUnitInstanceId(), myUnit, ctx);
        }

        protected void send(Event event) {
            if (event.targetId().equals(unit().id()) && event.payload().equals("SPECIAL")) {
                unit().application().send(
                        SimpleEvent.of(id(), MyUnit.ID,
                                       String.format("Message from %s -- %s", id(), unit().id())));
            }
            super.send(event);
        }

        @Override
        public void run() {

        }
    }

}