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

import java.util.List;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

    RepositoryService repositoryService = rule.getRepositoryService();
    FormService formService = rule.getFormService();

    ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("testProcess").singleResult();

    StartFormData startFormData = formService.getStartFormData(processDefinition.getId());

    List<FormField> formFields = startFormData.getFormFields();

    assertEquals(2, formFields.size());

    FormField field = formFields.get(0);
    assertEquals("name", field.getId());
    assertEquals("Name", field.getLabel());

    field = formFields.get(1);
    assertEquals("age", field.getId());
    assertEquals("Age", field.getLabel());
    assertEquals("30", field.getDefaultValue());
  }

}
