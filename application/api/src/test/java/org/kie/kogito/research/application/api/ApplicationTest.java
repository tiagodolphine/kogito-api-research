package org.kie.kogito.research.application.api;

import org.junit.jupiter.api.Test;
import org.kie.kogito.research.application.api.impl.AbstractApplication;
import org.kie.kogito.research.application.api.impl.AbstractUnit;
import org.kie.kogito.research.application.api.impl.AbstractUnitContainer;
import org.kie.kogito.research.application.api.impl.AbstractUnitInstance;
import org.kie.kogito.research.application.api.impl.SimpleEvent;
import org.kie.kogito.research.application.api.impl.SimpleUnitId;
import org.kie.kogito.research.application.api.impl.SimpleUnitInstanceId;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationTest {

    @Test
    public void test() {
        Application app = new MyApplication();

        MyUnitContainer myUnitContainer = app.get(MyUnitContainer.class);
        MyUnit myContextUnit = myUnitContainer.get(MyUnit.ID);
        MyUnitInstance instance = myContextUnit.createInstance(new MyContext());

        AnotherUnitContainer anotherUnitContainer = app.get(AnotherUnitContainer.class);
        AnotherUnit anotherMyContextUnit = anotherUnitContainer.get(ClassUnitId.of(AnotherUnit.class));
        AnotherUnitInstance anotherInstance = anotherMyContextUnit.createInstance(new MyContext());

        MyContextWithExecutionModel ctx = new MyContextWithExecutionModel();
        AnotherUnitInstance yetAnotherInstance = app.get(AnotherUnitContainer.class)
                .get(ClassUnitId.of(AnotherUnit.class))
                .createInstance(ctx);

        assertFalse(ctx.gotMessage);
        app.send(SimpleEvent.of(null, ClassUnitId.of(AnotherUnit.class), "Hello Message!"));
        assertTrue(ctx.gotMessage);


    }
    static class MyContext implements Context {}

    static class MyContextWithExecutionModel implements Context, ExecutionModel {

        public boolean gotMessage = false;

        @Override
        public void onEvent(Event event) {
            gotMessage = true;
        }
    }

    static class MyApplication extends AbstractApplication {{
        register(new MyUnitContainer(this));
        register(new AnotherUnitContainer(this));

    }}
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

        @Override
        public void run() {

        }
    }

}