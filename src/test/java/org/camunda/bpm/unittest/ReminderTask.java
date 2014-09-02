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
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.task.Task;

import static org.junit.Assert.assertEquals;

/**
 * @author Sebastian Menski
 */
public class ReminderTask implements JavaDelegate {

  public static String taskToReview;

  public void execute(DelegateExecution execution) throws Exception {
    taskToReview = (String) execution.getVariable("taskToReview");
    System.out.println("Please review task " + taskToReview);

    // test if correct task id was found
    TaskService taskService = execution.getProcessEngineServices().getTaskService();
    Task task = taskService.createTaskQuery().active().singleResult();
    assertEquals(taskToReview, task.getId());
  }

}
