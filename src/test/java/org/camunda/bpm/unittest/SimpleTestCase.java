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
import org.camunda.bpm.model.bpmn.instance.Definitions;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.Lane;
import org.camunda.bpm.model.bpmn.instance.LaneSet;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.bpmn.instance.Task;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Daniel Meyer
 *
 */
public class SimpleTestCase {

  @Rule
  public ProcessEngineRule rule = new ProcessEngineRule();

  protected BpmnModelInstance buildModel() {
    // Create Model
    BpmnModelInstance modelInstance = Bpmn.createEmptyModel();
    Definitions definitions = modelInstance.newInstance(Definitions.class);
    definitions.setId("definition");
    definitions.setTargetNamespace("http://camunda.com");
    modelInstance.setDefinitions(definitions);

    // Create Process
    Process process = modelInstance.newInstance(Process.class);
    process.setName("Process Name");
    process.setId("id12345");
    process.setExecutable(true);
    definitions.addChildElement(process);


    // Create laneset ( to hold lane )
    LaneSet laneset = modelInstance.newInstance(LaneSet.class);

    // Create lane
    Lane lane = modelInstance.newInstance(Lane.class);
    lane.setName("Swimlane Name");

    // Create start event
    StartEvent startEvent = modelInstance.newInstance(StartEvent.class);
    startEvent.setName("Start Event");
    startEvent.setId("id23456");
    startEvent.setCamundaAsync(true);
    process.addChildElement(startEvent);

    // Add reference to lane
    lane.getFlowNodeRefs().add(startEvent);

    // Create empty task
    Task task = modelInstance.newInstance(Task.class);
    task.setName("Task");
    task.setId("id34567");
    process.addChildElement(task);

    // Add reference to lane
    lane.getFlowNodeRefs().add(task);

    // Create end event
    EndEvent endEvent = modelInstance.newInstance(EndEvent.class);
    endEvent.setName("End Event");
    endEvent.setId("id45678");
    process.addChildElement(endEvent);

    // Add reference to lane
    lane.getFlowNodeRefs().add(endEvent);

    // Add lane to laneset
    laneset.addChildElement(lane);
    // Add laneset to process
    process.addChildElement(laneset);

    return modelInstance;
  }

  @Test
  public void shouldExecuteProcess() {
    BpmnModelInstance modelInstance = buildModel();
    Bpmn.validateModel(modelInstance);
    System.out.println(Bpmn.convertToString(modelInstance));

    RepositoryService repositoryService = rule.getProcessEngine().getRepositoryService();
    repositoryService.createDeployment().addModelInstance("testProcess.bpmn", modelInstance).deploy();

    RuntimeService runtimeService = rule.getProcessEngine().getRuntimeService();
    runtimeService.startProcessInstanceByKey("id12345");

    HistoryService historyService = rule.getProcessEngine().getHistoryService();
    assertEquals(1, historyService.createHistoricProcessInstanceQuery().processDefinitionKey("id12345").count());
  }

}
