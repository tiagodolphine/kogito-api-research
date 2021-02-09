package org.kie.kogito.research.processes.api.runtime;

import java.util.Map;

import org.kie.kogito.research.processes.api.ProcessInstance;

public interface ProcessRuntime {

    ProcessInstance startProcess(String processUnitId);

    ProcessInstance startProcess(String processUnitId, Map<String, Object> variables);

    ProcessInstance createProcessInstance(String processUnitId, Map<String, Object> variables);

    ProcessInstance startProcessInstance(String processInstanceId);

    ProcessInstance startProcessInstance(String processInstanceId, String trigger);

    void signalEvent(String type, Object event);

    void signalEvent(String type, Object event, String processInstanceId);

    void abortProcessInstance(String processInstanceId);
}
