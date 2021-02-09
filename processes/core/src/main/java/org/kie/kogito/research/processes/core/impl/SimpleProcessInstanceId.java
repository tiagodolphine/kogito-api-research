package org.kie.kogito.research.processes.core.impl;

import org.kie.kogito.research.processes.api.ProcessInstanceId;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class SimpleProcessInstanceId implements ProcessInstanceId {
    private final String value;

    public static SimpleProcessInstanceId create() {
        return new SimpleProcessInstanceId(UUID.randomUUID().toString());
    }

    public static SimpleProcessInstanceId fromString(String id) {
        return new SimpleProcessInstanceId(id);
    }

    private SimpleProcessInstanceId(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleProcessInstanceId that = (SimpleProcessInstanceId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
