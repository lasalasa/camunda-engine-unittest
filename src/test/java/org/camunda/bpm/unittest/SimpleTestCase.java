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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Daniel Meyer
 *
 */
public class SimpleTestCase {

  @Rule
  public ProcessEngineRule rule = new ProcessEngineRule();

  public static final Logger LOG = LoggerFactory.getLogger(SimpleTestCase.class);

  @Test
  @Deployment(resources = {"testProcess.bpmn"})
  public void shouldExecuteProcess() {

    LOG.info("Starting test case");

    RuntimeService runtimeService = rule.getRuntimeService();
    TaskService taskService = rule.getTaskService();

    LOG.debug("Got runtime- and taskService");

    ProcessInstance pi = runtimeService.startProcessInstanceByKey("testProcess");
    assertFalse("Process instance should not be ended", pi.isEnded());
    assertEquals(1, runtimeService.createProcessInstanceQuery().count());

    LOG.info("Process started");

    Task task = taskService.createTaskQuery().singleResult();
    assertNotNull("Task should exist", task);

    LOG.debug("Found task " + task);

    // complete the task
    taskService.complete(task.getId());

    LOG.info("Task completed");

    // now the process instance should be ended
    assertEquals(0, runtimeService.createProcessInstanceQuery().count());

    LOG.info("Process ended");

  }

}
