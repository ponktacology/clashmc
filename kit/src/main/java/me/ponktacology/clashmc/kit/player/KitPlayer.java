package me.ponktacology.clashmc.kit.player;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.api.util.TimeUtil;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.player.BukkitPlayerWrapper;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.util.InventoryUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.kit.kit.Kit;
import org.bukkit.entity.Player;



import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@Entity(collection = "players", database = "kit")
public class KitPlayer extends BukkitPlayerWrapper {

  private final Map<String, Long> lastKitUseTimeStamp = new HashMap<>();

  public KitPlayer(UUID uuid, String name) {
    super(uuid, name);
  }

  public boolean hasAccess( Kit kit) {
    Optional<CorePlayer> corePlayerOptional =
        CorePlugin.INSTANCE.getPlayerCache().get(this.getPlayer());

    if (!corePlayerOptional.isPresent()) {
      return false;
    }

    CorePlayer corePlayer = corePlayerOptional.get();

    if (corePlayer.isStaff()) return true;

    return corePlayer.getRanks().stream().anyMatch(rank -> kit.getAllowedRanks().contains(rank));
  }

  public boolean handleKit( Kit kit) {
    Player player = this.getPlayer();

    Optional<CorePlayer> corePlayerOptional =
        CorePlugin.INSTANCE.getPlayerCache().get(this.getPlayer());

    if (!corePlayerOptional.isPresent()) {
      return false;
    }

    CorePlayer corePlayer = corePlayerOptional.get();

    if (!corePlayer.isStaff()) {
      if (!kit.isEnabled()) {
        player.sendMessage(Text.colored("&cZestaw jest wyłączony."));
        return false;
      }

      if (!hasAccess(kit)) {
        player.sendMessage(Text.colored("&cNie masz dostępu do tego zestawu."));
        return false;
      }

      if (lastKitUseTimeStamp.containsKey(kit.getName())) {
        long lastUse = lastKitUseTimeStamp.get(kit.getName());

        long timeLeft = lastUse + kit.getDelay() - System.currentTimeMillis();

        if (timeLeft > 0) {
          player.sendMessage(
              Text.colored(
                  "&cKolejny raz ten zestaw możesz odebrać za "
                      + TimeUtil.formatTimeMillis(timeLeft)
                      + "."));
          return false;
        }
      }

      lastKitUseTimeStamp.put(kit.getName(), System.currentTimeMillis());
    }

    InventoryUtil.addItem(player, kit.getItems());

    player.sendMessage(Text.colored("&aOtrzymano zestaw."));

    return true;
  }

  @Override
  public boolean equals( Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    KitPlayer that = (KitPlayer) o;

    return this.getUuid().equals(that.getUuid());
  }
}
