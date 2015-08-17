/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.unittest;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;

import java.util.Collections;

import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Daniel Meyer
 * @author Martin Schimak
 */
public class SimpleTestCase {

  @Rule
  public ProcessEngineRule rule = new ProcessEngineRule();

  @Test
  @Deployment(resources = {"testProcess.bpmn", "testDecisionProperty.dmn"})
  public void shouldAccessValueByProperty() {
    VariableMap variables = Variables.createVariables();
    variables.putValue("task", Collections.singletonMap("foo", "a"));

    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("testProcess", variables);
    String decisionResult = (String) runtimeService().getVariable(processInstance.getId(), "decisionResult");
    assertThat(decisionResult).isEqualTo("a");

    variables.putValue("task", Collections.singletonMap("foo", "b"));

    processInstance = runtimeService().startProcessInstanceByKey("testProcess", variables);
    decisionResult = (String) runtimeService().getVariable(processInstance.getId(), "decisionResult");
    assertThat(decisionResult).isEqualTo("b");
  }

  @Test
  @Deployment(resources = {"testProcess.bpmn", "testDecisionKey.dmn"})
  public void shouldAccessValueByKey() {
    VariableMap variables = Variables.createVariables();
    variables.putValue("task", Collections.singletonMap("foo", "a"));

    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("testProcess", variables);
    String decisionResult = (String) runtimeService().getVariable(processInstance.getId(), "decisionResult");
    assertThat(decisionResult).isEqualTo("a");

    variables.putValue("task", Collections.singletonMap("foo", "b"));

    processInstance = runtimeService().startProcessInstanceByKey("testProcess", variables);
    decisionResult = (String) runtimeService().getVariable(processInstance.getId(), "decisionResult");
    assertThat(decisionResult).isEqualTo("b");
  }

}
