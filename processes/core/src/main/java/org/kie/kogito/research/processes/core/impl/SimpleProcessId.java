package org.kie.kogito.research.processes.core.impl;

import org.kie.kogito.research.processes.api.ProcessId;

import java.util.Objects;

public class SimpleProcessId implements ProcessId {
    private final String value;

    public static SimpleProcessId fromString(String id) {
        return new SimpleProcessId(id);
    }

    private SimpleProcessId(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleProcessId that = (SimpleProcessId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "SimpleProcessId{" +
                "'" + value + '\'' +
                '}';
    }
}
