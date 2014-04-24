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

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Daniel Meyer
 *
 */
public class SimpleTestCase {

  @Rule
  public ProcessEngineRule rule = new ProcessEngineRule();

  @Before
  public void createUser() {
    IdentityService identityService = rule.getIdentityService();
    Group finance = identityService.newGroup("finance");
    identityService.saveGroup(finance);
    User kermit = identityService.newUser("kermit");
    identityService.saveUser(kermit);
    User johnny = identityService.newUser("johnny");
    identityService.saveUser(johnny);
    User marry = identityService.newUser("marry");
    identityService.saveUser(marry);
    identityService.createMembership(kermit.getId(), finance.getId());
    identityService.createMembership(johnny.getId(), finance.getId());
    identityService.createMembership(marry.getId(), finance.getId());
  }

  @Test
  @Deployment(resources = {"testProcess.bpmn"})
  public void shouldSetCandidateUsers() {

    RuntimeService runtimeService = rule.getRuntimeService();
    TaskService taskService = rule.getTaskService();

    runtimeService.startProcessInstanceByKey("testProcess");

    // get task A
    Task taskA = taskService.createTaskQuery().singleResult();
    assertNotNull(taskA);

    // set assignee
    taskA.setAssignee("johnny");
    taskService.saveTask(taskA);

    // complete task to execute listener
    taskService.complete(taskA.getId());

    // get task B
    Task taskB = taskService.createTaskQuery().singleResult();
    assertNotNull(taskB);

    // get process variable
    List<String> candidateUserIds = (List<String>) runtimeService.getVariable(taskB.getExecutionId(), "candidateUserIds");
    assertNotNull(candidateUserIds);
    assertEquals(2, candidateUserIds.size());
    assertTrue(candidateUserIds.contains("kermit"));
    assertFalse(candidateUserIds.contains("johnny"));
    assertTrue(candidateUserIds.contains("marry"));

    taskService.complete(taskB.getId());

  }

}
