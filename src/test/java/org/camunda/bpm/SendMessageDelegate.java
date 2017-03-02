package org.camunda.bpm;

import java.util.HashMap;
import java.util.Map;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;


public class SendMessageDelegate implements JavaDelegate {

  public void execute(DelegateExecution execution) throws Exception {
    String businessKey = (String) execution.getVariable("businessKey");

    Map<String, Object> correlationVariables = new HashMap<String, Object>();
    Object msisdn1 = execution.getVariable("MSISDN1");
    correlationVariables.put("MSISDN1", msisdn1);

    if (businessKey != null) {
      // correlate with business key
      execution.getProcessEngineServices().getRuntimeService()
        .correlateMessage("TestMessage", businessKey, correlationVariables);
    }
    else {
      // correlate without business key
      execution.getProcessEngineServices().getRuntimeService()
        .correlateMessage("TestMessage", correlationVariables);
    }
  }

}
