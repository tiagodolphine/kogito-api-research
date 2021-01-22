package org.kie.kogito.research.application.api;

import java.util.Objects;

public class ClassUnitId implements UnitId {
    private final Class<?> cls;

    protected ClassUnitId(Class<?> cls) {
        this.cls = cls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClassUnitId that = (ClassUnitId) o;
        return Objects.equals(cls, that.cls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cls);
    }

    public static ClassUnitId of(Class<?> cls) {
        return new ClassUnitId(cls);
    }

}
