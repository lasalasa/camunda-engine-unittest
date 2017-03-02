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

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;

import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
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
  @Deployment(resources = {"send_message.bpmn", "receive_message.bpmn"})
  public void shouldCorrelateMessage() {
    // given
    VariableMap variables = Variables.putValue("MSISDN1", "1234");
    ProcessInstance receiveMessage1 = runtimeService().startProcessInstanceByKey("receiveMessage", variables);
    ProcessInstance receiveMessage2 = runtimeService().startProcessInstanceByKey("receiveMessage", variables);
    ProcessInstance receiveMessage3 = runtimeService().startProcessInstanceByKey("receiveMessage", variables);

    // when
    runtimeService().deleteProcessInstance(receiveMessage1.getProcessInstanceId(), "test");
    runtimeService().deleteProcessInstance(receiveMessage2.getProcessInstanceId(), "test");

    // then
    assertThat(receiveMessage1).isEnded();
    assertThat(receiveMessage2).isEnded();

    // when
    ProcessInstance sendMessage = runtimeService().startProcessInstanceByKey("sendMessage", variables);

    // then
    assertThat(sendMessage).isEnded();
    assertThat(receiveMessage3).isEnded();
  }

  @Test
  @Deployment(resources = {"send_message.bpmn", "receive_message.bpmn"})
  public void shouldCorrelateMessageWithBusinessKey() {
    // given
    VariableMap variables = Variables.putValue("MSISDN1", "1234");
    ProcessInstance receiveMessage1 = runtimeService().startProcessInstanceByKey("receiveMessage", "receive1", variables);
    ProcessInstance receiveMessage2 = runtimeService().startProcessInstanceByKey("receiveMessage", "receive2", variables);
    ProcessInstance receiveMessage3 = runtimeService().startProcessInstanceByKey("receiveMessage", "receive3", variables);

    // when
    runtimeService().deleteProcessInstance(receiveMessage1.getProcessInstanceId(), "test");
    runtimeService().deleteProcessInstance(receiveMessage2.getProcessInstanceId(), "test");

    // then
    assertThat(receiveMessage1).isEnded();
    assertThat(receiveMessage2).isEnded();

    // when
    variables.putValue("businessKey", "receive3");
    ProcessInstance sendMessage = runtimeService().startProcessInstanceByKey("sendMessage", variables);

    // then
    assertThat(sendMessage).isEnded();
    assertThat(receiveMessage3).isEnded();
  }

}
