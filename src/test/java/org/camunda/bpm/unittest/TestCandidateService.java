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

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.task.IdentityLink;

import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * @author Sebastian Menski
 */
public class TestCandidateService implements TaskListener {

  public void notify(DelegateTask delegateTask) {
    IdentityService identityService = delegateTask.getProcessEngineServices().getIdentityService();

    for (IdentityLink candidate : delegateTask.getCandidates()) {
      if (candidate.getUserId() != null) {
        assertFalse(candidate.getUserId().equals("johnny"));
      }
      else if (candidate.getGroupId() != null) {
        List<User> members = identityService.createUserQuery().memberOfGroup(candidate.getGroupId()).list();
        for (User member : members) {
          assertFalse(member.getId().equals("johnny"));
        }
      }
    }
  }

}
