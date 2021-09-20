package me.ponktacology.clashmc.core.player;

import lombok.NoArgsConstructor;
import me.ponktacology.clashmc.api.player.PlayerWrapper;
import me.ponktacology.clashmc.core.CorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@NoArgsConstructor
public class BukkitPlayerWrapper extends PlayerWrapper {

  public BukkitPlayerWrapper(UUID uuid, String name) {
    super(uuid, name);
  }

  public void save() {
    CorePlugin.INSTANCE.getCacheNetworkService().save(this);
    CorePlugin.INSTANCE.getDataService().save(this);
  }

  public Player getPlayer() {
    return Bukkit.getServer().getPlayer(this.getUuid());
  }

  public void sendMessage(String message) {
    Player player = getPlayer();

    if (player == null) return;

    player.sendMessage(message);
  }

  public boolean isOnThisServer() {
    return getPlayer() != null;
  }
}
