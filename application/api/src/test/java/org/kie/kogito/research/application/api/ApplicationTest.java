package org.kie.kogito.research.application.api;

import org.junit.jupiter.api.Test;
import org.kie.kogito.research.application.api.impl.AbstractApplication;
import org.kie.kogito.research.application.api.impl.AbstractUnit;
import org.kie.kogito.research.application.api.impl.AbstractUnitContainer;
import org.kie.kogito.research.application.api.impl.AbstractUnitInstance;
import org.kie.kogito.research.application.api.impl.SimpleUnitId;
import org.kie.kogito.research.application.api.impl.SimpleUnitInstanceId;

class ApplicationTest {

    @Test
    public void test() {
        Application app = new MyApplication();

        MyUnitContainer myUnitContainer = app.get(MyUnitContainer.class);
        MyUnit myContextUnit = myUnitContainer.get(MyContext.class);
        MyUnitInstance instance = myContextUnit.createInstance(new MyContext());

        AnotherUnitContainer anotherUnitContainer = app.get(AnotherUnitContainer.class);
        AnotherUnit anotherMyContextUnit = anotherUnitContainer.get(MyContext.class);
        AnotherUnitInstance anotherInstance = anotherMyContextUnit.createInstance(new MyContext());

    }
    static class MyContext implements Context {}

    static class MyApplication extends AbstractApplication {{
        register(new AnotherUnitContainer(this));

    }}
    static class MyUnitContainer extends AbstractUnitContainer<MyUnit> {
        MyUnitContainer(Application app) {
            super(app);
            register(new MyUnit());
        }

    }
    static class MyUnit extends AbstractUnit<MyUnitInstance> {
        public MyUnit() {
            super(new SimpleUnitId());
        }
        public MyUnitInstance createInstance(Context ctx) {
            return register(new MyUnitInstance(this));
        }

    }
    static class MyUnitInstance extends AbstractUnitInstance {
        public MyUnitInstance(MyUnit myUnit) {
            super(new SimpleUnitInstanceId(), myUnit);
        }
    }


    static class AnotherUnitContainer extends AbstractUnitContainer<AnotherUnit> {
        AnotherUnitContainer(Application app) {
            super(app);
            register(new AnotherUnit());
        }
    }

    static class AnotherUnit extends AbstractUnit<AnotherUnitInstance> {
        public AnotherUnit() {
            super(new SimpleUnitId());
        }
        public AnotherUnitInstance createInstance(Context ctx) {
            return register(new AnotherUnitInstance(this));
        }
    }

    static class AnotherUnitInstance extends AbstractUnitInstance {
        public AnotherUnitInstance(AnotherUnit myUnit) {
            super(new SimpleUnitInstanceId(), myUnit);
        }
    }

}