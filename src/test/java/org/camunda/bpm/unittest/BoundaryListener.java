/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camunda.bpm.unittest;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.instance.Activity;
import org.camunda.bpm.model.bpmn.instance.BoundaryEvent;

/**
 * @author Sebastian Menski
 */
public class BoundaryListener implements ExecutionListener {

  public void notify(DelegateExecution execution) throws Exception {
    // get boundary event with model API
    BoundaryEvent boundaryElement = (BoundaryEvent) execution.getBpmnModelElementInstance();
    // get task to which the boundary event is attached
    Activity taskElement = boundaryElement.getAttachedTo();

    TaskService taskService = execution.getProcessEngineServices().getTaskService();

    // query for task in current process instance
    Task task = taskService.createTaskQuery()
      .processInstanceId(execution.getProcessInstanceId())
      .taskDefinitionKey(taskElement.getId())
      .singleResult();

    // save task id
    execution.setVariable("taskToReview", task.getId());
  }

}
