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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;

import org.camunda.bpm.engine.impl.bpmn.deployer.BpmnDeployer;
import org.camunda.bpm.engine.impl.persistence.deploy.Deployer;
import org.camunda.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.camunda.bpm.engine.impl.persistence.entity.ResourceEntity;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Process;

public class PreDeployer implements Deployer {

  public void deploy(DeploymentEntity deployment) {
    for (String resourceName : deployment.getResources().keySet()) {
      if (isBpmnResource(resourceName)) {
        ResourceEntity resource = deployment.getResource(resourceName);
        modifyProcessKey(resource);
      }
    }
  }

  protected void modifyProcessKey(ResourceEntity resource) {
    byte[] bytes = resource.getBytes();
    ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
    BpmnModelInstance modelInstance = Bpmn.readModelFromStream(inputStream);

    Collection<Process> processes = modelInstance.getModelElementsByType(Process.class);
    for (Process process : processes) {
      process.setId(process.getId() + "-MySuffix");
    }

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Bpmn.writeModelToStream(outputStream, modelInstance);

    bytes = outputStream.toByteArray();
    resource.setBytes(bytes);
  }

  protected boolean isBpmnResource(String resourceName) {
    for (String suffix : BpmnDeployer.BPMN_RESOURCE_SUFFIXES) {
      if (resourceName.endsWith(suffix)) {
        return true;
      }
    }
    return false;
  }

}
