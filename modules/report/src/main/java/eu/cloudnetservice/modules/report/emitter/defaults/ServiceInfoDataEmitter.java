/*
 * Copyright 2019-2022 CloudNetService team & contributors
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

package eu.cloudnetservice.modules.report.emitter.defaults;

import eu.cloudnetservice.driver.CloudNetDriver;
import eu.cloudnetservice.driver.service.ServiceInfoSnapshot;
import eu.cloudnetservice.modules.report.emitter.ReportDataWriter;
import eu.cloudnetservice.modules.report.emitter.SpecificReportDataEmitter;
import java.util.Collection;
import lombok.NonNull;

public final class ServiceInfoDataEmitter extends SpecificReportDataEmitter<ServiceInfoSnapshot> {

  public ServiceInfoDataEmitter() {
    super((writer, snapshots) -> writer.appendString("Services (").appendInt(snapshots.size()).appendString("):"));
  }

  @Override
  public @NonNull Collection<ServiceInfoSnapshot> collectData() {
    return CloudNetDriver.instance().cloudServiceProvider().services();
  }

  @Override
  public @NonNull ReportDataWriter emitData(@NonNull ReportDataWriter writer, @NonNull ServiceInfoSnapshot value) {
    return writer.beginSection(value.name()).appendAsJson(value).endSection();
  }
}
