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

import java.util.HashMap;
import java.util.Map;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Daniel Meyer
 *
 */
public class SimpleTestCase {

  @Rule
  public ProcessEngineRule rule = new ProcessEngineRule();

  @Test
  @Deployment(resources = {"testProcess.bpmn"})
  public void shouldExecuteProcess() {

    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("foo", "bar");

    RuntimeService runtimeService = rule.getRuntimeService();
    HistoryService historyService = rule.getHistoryService();

    runtimeService.startProcessInstanceByKey("testProcess", variables);
    // process should be ended
    assertEquals(0, runtimeService.createProcessInstanceQuery().count());

    HistoricVariableInstance foo = historyService.createHistoricVariableInstanceQuery().singleResult();
    assertNotNull(foo);
    assertEquals("bar", foo.getValue());
  }

}
