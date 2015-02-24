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
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.taskService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.impl.util.ClockUtil;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Daniel Meyer
 * @author Martin Schimak
 */
public class SimpleTestCase {

  @Rule
  public ProcessEngineRule rule = new ProcessEngineRule();

  public String[] taskNames;
  public Date[] createDates;

  @BeforeClass
  public static void enableLogging() {
    LogFactory.useStdOutLogging();
  }

  @Before
  public void createTasks() {
    taskNames = new String[] { "Z", "C", "A", "B" };
    createDates = new Date[] {
      new Date(1424497434),
      new Date(1423597434),
      new Date(1424790034),
      new Date(1414787434)
    };

    for (int i = 0; i < taskNames.length; i++) {
      ClockUtil.setCurrentTime(createDates[i]);
      Task task = taskService().newTask();
      task.setName(taskNames[i]);
      taskService().saveTask(task);
    }

    Arrays.sort(taskNames);
    Arrays.sort(createDates);
  }

  @Test
  public void shouldOrderTasksByName() {
    List<Task> tasks = taskService().createTaskQuery().orderByTaskName().asc().list();
    for (int i = 0; i < taskNames.length; i++) {
      assertThat(taskNames[i]).isEqualTo(tasks.get(i).getName());
    }

    tasks = taskService().createTaskQuery().orderByTaskName().desc().list();
    for (int i = 0; i < taskNames.length; i++) {
      assertThat(taskNames[taskNames.length - 1 - i]).isEqualTo(tasks.get(i).getName());
    }
  }

  @Test
  public void shouldOrderTasksByCreateTime() {
    List<Task> tasks = taskService().createTaskQuery().orderByTaskCreateTime().asc().list();
    for (int i = 0; i < createDates.length; i++) {
      assertThat(createDates[i]).isEqualTo(tasks.get(i).getCreateTime());
    }

    tasks = taskService().createTaskQuery().orderByTaskCreateTime().desc().list();
    for (int i = 0; i < createDates.length; i++) {
      assertThat(createDates[createDates.length - 1 - i]).isEqualTo(tasks.get(i).getCreateTime());
    }
  }

  @After
  public void deleteTasks() {
    for (Task task : taskService().createTaskQuery().list()) {
      taskService().deleteTask(task.getId());
    }

  }

}
