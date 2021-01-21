package org.kie.kogito.research.application.api;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class ApplicationTest {

    @Test
    public void test() {
        Application app = new MyApplication();
        MyUnitContainer myUnitContainer = app.get(MyUnitContainer.class);
        MyUnit<MyContext> myContextUnit = myUnitContainer.get(MyContext.class);
        MyUnitInstance<MyContext> instance = myContextUnit.createInstance(new MyContext());
        instance.start();
    }

    static class MyApplication implements Application {

        Map<Class<?>, Object> containers = new HashMap<>();
        {
            containers.put(MyUnitContainer.class, new MyUnitContainer());
        }

        @Override
        public <T extends UnitContainer> T get(Class<T> ctr) {
            return (T) containers.get(ctr);
        }
    }


    static class MyUnit<C extends Context> implements Unit<C> {
        public MyUnitInstance<C> createInstance(C ctx) {
            return new MyUnitInstance<C>();
        }
    }

    static class MyUnitInstance<C extends Context> implements UnitInstance<C> {
        public void start() {}
        public void stop() {}
        public void send(Event msg) {}
    }

    static class MyUnitContainer implements UnitContainer {

        @Override
        public <C extends Context> MyUnit<C> get(Class<C> unit) {
            return new MyUnit<>();
        }
    }
    static class MyContext implements Context {}

}