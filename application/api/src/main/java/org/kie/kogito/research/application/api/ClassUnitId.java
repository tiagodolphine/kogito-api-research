package org.kie.kogito.research.application.api;

public class ClassUnitId implements UnitId {
    private final Class<?> cls;

    protected ClassUnitId(Class<?> cls) {
        this.cls = cls;
    }

    public static ClassUnitId of(Class<?> cls) {
        return new ClassUnitId(cls);
    }

}
