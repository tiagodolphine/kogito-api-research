package org.kie.kogito.research.processes.core.impl;

import java.util.Objects;
import java.util.UUID;

import org.kie.kogito.research.processes.api.NodeInstanceId;
import org.kie.kogito.research.processes.api.ProcessInstanceId;

public class SimpleNodeInstanceId implements NodeInstanceId {
    private final String value;

    public static SimpleNodeInstanceId create() {
        return new SimpleNodeInstanceId(UUID.randomUUID().toString());
    }

    public static SimpleNodeInstanceId fromString(String id) {
        return new SimpleNodeInstanceId(id);
    }

    private SimpleNodeInstanceId(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleNodeInstanceId that = (SimpleNodeInstanceId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
