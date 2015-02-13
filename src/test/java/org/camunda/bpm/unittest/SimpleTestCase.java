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

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.runtimeService;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.task;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
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
  @Deployment(resources = {"process1.bpmn", "process2.bpmn", "process3.bpmn"})
  public void shouldExecuteProcess() {
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("error", false);

    // Given we create a new process instance
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("Process_1", variables);

    // And there should exist just a single task within that process instance
    assertThat(task(processInstance)).isNotNull();

    // And the process variables exist
    assertThat(processInstance).variables()
      .containsEntry("hello", "world")
      .containsEntry("a", "?")
      .containsEntry("b", "");
  }

  @Test
  @Deployment(resources = {"process1.bpmn", "process2.bpmn", "process3.bpmn"})
  public void shouldThrowError() {
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("error", true);

    // Given we create a new process instance
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("Process_1", variables);

    // And there should exist just a single task within that process instance
    assertThat(task(processInstance)).isNotNull();

    // And the process variables exist
    assertThat(processInstance).variables()
      .containsEntry("hello", "world")
      .containsEntry("a", "?")
      .containsEntry("b", "");
  }
}
