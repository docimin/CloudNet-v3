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

package de.dytanic.cloudnet.ext.bridge.platform.bungeecord;

import de.dytanic.cloudnet.ext.bridge.platform.PlatformBridgeManagement;
import de.dytanic.cloudnet.ext.bridge.platform.bungeecord.command.BungeeCordCloudCommand;
import de.dytanic.cloudnet.ext.bridge.platform.bungeecord.command.BungeeCordHubCommand;
import de.dytanic.cloudnet.ext.bridge.player.NetworkPlayerProxyInfo;
import de.dytanic.cloudnet.wrapper.Wrapper;
import java.util.Arrays;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public final class BungeeCordBridgePlugin extends Plugin {

  @Override
  public void onEnable() {
    // init the management
    PlatformBridgeManagement<ProxiedPlayer, NetworkPlayerProxyInfo> management = new BungeeCordBridgeManagement();
    management.registerServices(Wrapper.getInstance().getServicesRegistry());
    management.postInit();
    // register the listeners
    ProxyServer.getInstance().getPluginManager().registerListener(
      this,
      new BungeeCordPlayerManagementListener(this, management));
    // register the cloud command
    ProxyServer.getInstance().getPluginManager().registerCommand(this, new BungeeCordCloudCommand(management));
    // register the hub command if requested
    if (!management.getConfiguration().getHubCommandNames().isEmpty()) {
      // convert to an array for easier access
      String[] names = management.getConfiguration().getHubCommandNames().toArray(new String[0]);
      // register the command
      ProxyServer.getInstance().getPluginManager().registerCommand(this, new BungeeCordHubCommand(
        management,
        names[0],
        names.length > 1 ? Arrays.copyOfRange(names, 1, names.length) : new String[0]));
    }
  }
}