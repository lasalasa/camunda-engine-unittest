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

import static org.junit.Assert.assertEquals;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Daniel Meyer
 *
 */
public class SimpleTestCase {

  @Rule
  public ProcessEngineRule rule = new ProcessEngineRule();

  protected BpmnModelInstance createProcess(String id, String name) {
    BpmnModelInstance modelInstance = Bpmn.createProcess("testProcess").done();
    ModelElementInstance process = modelInstance.getModelElementById("testProcess");
    StartEvent startEvent = modelInstance.newInstance(StartEvent.class);
    startEvent.setId(id);
    startEvent.setName(name);
    process.addChildElement(startEvent);
    return modelInstance;
  }

  @Test
  public void shouldExecuteProcess() {
    BpmnModelInstance modelInstance = createProcess("aNormalId", "A Name with whitespaces and even line breaks\n!");
    Bpmn.validateModel(modelInstance);
    System.out.println(Bpmn.convertToString(modelInstance));

    RepositoryService repositoryService = rule.getProcessEngine().getRepositoryService();
    repositoryService.createDeployment().addModelInstance("testProcess.bpmn", modelInstance).deploy();

    RuntimeService runtimeService = rule.getProcessEngine().getRuntimeService();
    runtimeService.startProcessInstanceByKey("testProcess");

    HistoryService historyService = rule.getProcessEngine().getHistoryService();
    assertEquals(1, historyService.createHistoricProcessInstanceQuery().processDefinitionKey("testProcess").count());
  }

}
