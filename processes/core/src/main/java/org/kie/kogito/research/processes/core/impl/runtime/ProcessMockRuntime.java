package org.kie.kogito.research.processes.core.impl.runtime;

import java.util.Map;

import org.kie.kogito.research.processes.api.ProcessInstance;
import org.kie.kogito.research.processes.api.runtime.ProcessRuntime;

public class ProcessMockRuntime implements ProcessRuntime {


    //subscribe for events
    public void init(){

    }


    //handle events

    @Override
    public ProcessInstance startProcess(String processUnitId) {
        return null;
    }

    @Override
    public ProcessInstance startProcess(String processUnitId, Map<String, Object> variables) {
        return null;
    }

    @Override
    public ProcessInstance createProcessInstance(String processUnitId, Map<String, Object> variables) {
        return null;
    }

    @Override
    public ProcessInstance startProcessInstance(String processInstanceId) {
        return null;
    }

    @Override
    public ProcessInstance startProcessInstance(String processInstanceId, String trigger) {
        return null;
    }

    @Override
    public void signalEvent(String type, Object event) {

    }

    @Override
    public void signalEvent(String type, Object event, String processInstanceId) {

    }

    @Override
    public void abortProcessInstance(String processInstanceId) {

    }
}
