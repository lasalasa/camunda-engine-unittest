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
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.task.IdentityLink;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sebastian Menski
 */
public class CalculateCandidateService implements TaskListener {

  public void notify(DelegateTask delegateTask) {
    IdentityService identityService = delegateTask.getProcessEngineServices().getIdentityService();

    // get assignee of the task (which completed the task
    String assignee = delegateTask.getAssignee();

    List<String> candidateUserIds = new ArrayList<String>();

    for (IdentityLink candidate : delegateTask.getCandidates()) {
      if (candidate.getUserId() != null) {
        if (!assignee.equals(candidate.getUserId())) {
          candidateUserIds.add(candidate.getUserId());
        }
      }
      else if (candidate.getGroupId() != null) {
        List<User> members = identityService.createUserQuery().memberOfGroup(candidate.getGroupId()).list();
        for (User member : members) {
          if (!assignee.equals(member.getId())) {
            candidateUserIds.add(member.getId());
          }
        }
      }
    }

    RuntimeService runtimeService = delegateTask.getProcessEngineServices().getRuntimeService();
    runtimeService.setVariable(delegateTask.getExecutionId(), "candidateUserIds", candidateUserIds);
  }

}
