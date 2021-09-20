package me.ponktacology.clashmc.core.punishment.menu.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.util.Console;
import me.ponktacology.clashmc.api.util.TimeUtil;
import me.ponktacology.clashmc.api.util.Unknown;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.player.factory.CorePlayerFactory;
import me.ponktacology.clashmc.core.punishment.Punishment;
import me.ponktacology.clashmc.core.punishment.PunishmentType;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class AltButton extends Button {

  private final CorePlayerFactory playerFactory;

  private final CorePlayerCache playerCache;

  private final CorePlayer corePlayer;

  public List<String> formatPunishment(PunishmentType type) {
    Optional<Punishment> punishmentOptional = corePlayer.getActivePunishment(type);

    if (!punishmentOptional.isPresent()) {
      return Collections.singletonList("&e" + type.getFormattedName() + ": &cNie");
    }

    Punishment punishment = punishmentOptional.get();

    String formattedIssuerName;
    if (punishment.getAddedBy().equals(Console.UUID)) {
      formattedIssuerName = Console.FORMATTED_NAME;
    } else {
      Optional<CorePlayer> issuerPlayerOptional = playerFactory.load(punishment.getAddedBy());

      formattedIssuerName =
          issuerPlayerOptional.map(CorePlayer::getFormattedName).orElse(Unknown.FORMATTED_NAME);
    }

    return Arrays.asList(
        "&e" + type.getFormattedName() + ": &aTak",
        "  &eAdministrator: &f" + formattedIssuerName,
        "  &ePowód: &f" + punishment.getReason(),
        "  &eWygasa: &f"
            + (punishment.hasExpired() ? "już wygasł" : punishment.getFormattedExpireDate()));
  }

  @Override
  public ItemStack getButtonItem(Player player) {
    return new ItemBuilder(Material.SKULL_ITEM)
        .name("&a" + corePlayer.getName())
        .skull(corePlayer.getName())
        .lore(
            "&eOnline: &f" + playerCache.isOnline(corePlayer),
            "&eOstatnio online: &f" + TimeUtil.formatTimeMillisToDate(corePlayer.getJoinTime()))
        .lore(this.formatPunishment(PunishmentType.BLACKLIST))
        .lore(this.formatPunishment(PunishmentType.BAN))
        .lore(this.formatPunishment(PunishmentType.MUTE))
        .build();
  }
}
