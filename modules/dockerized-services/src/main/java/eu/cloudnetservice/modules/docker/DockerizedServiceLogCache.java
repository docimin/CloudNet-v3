/*
 * Copyright 2019-2021 CloudNetService team & contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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

package eu.cloudnetservice.modules.docker;

import com.github.dockerjava.api.model.Frame;
import de.dytanic.cloudnet.CloudNet;
import de.dytanic.cloudnet.service.CloudService;
import de.dytanic.cloudnet.service.ServiceConsoleLogCache;
import de.dytanic.cloudnet.service.defaults.log.AbstractServiceLogCache;
import java.nio.charset.StandardCharsets;
import lombok.NonNull;

public class DockerizedServiceLogCache extends AbstractServiceLogCache {

  public DockerizedServiceLogCache(@NonNull CloudNet cloudNet, @NonNull CloudService service) {
    super(cloudNet, service);
  }

  @Override
  public @NonNull ServiceConsoleLogCache update() {
    return this;
  }

  public void handle(@NonNull Frame frame) {
    switch (frame.getStreamType()) {
      case STDERR -> this.handleItem(new String(frame.getPayload(), StandardCharsets.UTF_8), true);
      case STDOUT -> this.handleItem(new String(frame.getPayload(), StandardCharsets.UTF_8), false);
      default -> {
      }
    }
  }

  @Override
  protected void handleItem(@NonNull String content, boolean comesFromErrorStream) {
    if (content.contains("\n") || content.contains("\r")) {
      for (var input : content.split("\r")) {
        for (var text : input.split("\n")) {
          if (!text.trim().isEmpty()) {
            super.handleItem(text, comesFromErrorStream);
          }
        }
      }
    }
  }
}