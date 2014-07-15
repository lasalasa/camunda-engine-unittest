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

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

/**
 * @author Sebastian Menski
 */
public class TestDelegate implements JavaDelegate {

  public static boolean executed = false;

  public void execute(DelegateExecution delegateExecution) throws Exception {
    Boolean skip = (Boolean) delegateExecution.getVariable("skip");
    if (skip != null && skip) {
      RuntimeService runtimeService = delegateExecution.getProcessEngineServices().getRuntimeService();
      runtimeService.correlateMessage("skipTask");
    }
    else {
      executed = true;
    }
  }

}
