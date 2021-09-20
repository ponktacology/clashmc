package me.ponktacology.clashmc.core.punishment.menu.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.util.Console;
import me.ponktacology.clashmc.api.util.TimeUtil;
import me.ponktacology.clashmc.api.util.Unknown;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.factory.CorePlayerFactory;
import me.ponktacology.clashmc.core.punishment.Punishment;
import me.ponktacology.clashmc.core.punishment.PunishmentType;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.core.util.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HistoryButton extends Button {


  private final Punishment punishment;

  private final CorePlayerFactory playerFactory;

  @Override
  public ItemStack getButtonItem(Player player) {
    List<String> lore = new ArrayList<>();

    String formattedIssuerName;
    if (punishment.getAddedBy().equals(Console.UUID)) {
      formattedIssuerName = Console.FORMATTED_NAME;
    } else {
      Optional<CorePlayer> issuerPlayerOptional = playerFactory.load(punishment.getAddedBy());

      formattedIssuerName =
          issuerPlayerOptional.map(CorePlayer::getFormattedName).orElse(Unknown.FORMATTED_NAME);
    }

    lore.add("&eAdministrator: &f" + formattedIssuerName);
    lore.add("&ePowód: &f" + punishment.getReason());
    lore.add("&eWygasa: &f"
            + (punishment.hasExpired() ? "już wygasł" : punishment.getFormattedExpireDate()));
    lore.add("&eData: &f" + TimeUtil.formatTimeMillisToDate(punishment.getAddedOn()));

    if (punishment.isRemoved() && punishment.getType() != PunishmentType.KICK) {
      if (punishment.getAddedBy().equals(Console.UUID)) {
        formattedIssuerName = Console.FORMATTED_NAME;
      } else {
        Optional<CorePlayer> issuerPlayerOptional = playerFactory.load(punishment.getAddedBy());

        formattedIssuerName =
            issuerPlayerOptional.map(CorePlayer::getFormattedName).orElse(Unknown.FORMATTED_NAME);
      }

      lore.add(Text.SB_BAR);
      lore.add("&eAnulowany przez: &f" + formattedIssuerName);
      lore.add("&ePowód: &f" + punishment.getRemoveReason());
      lore.add("&eData: &f" + TimeUtil.formatTimeMillisToDate(punishment.getRemovedOn()));
    }

    return new ItemBuilder(Material.INK_SACK)
        .name("&e" + punishment.getType().getFormattedName())
        .durability(punishment.isRemoved() ? 8 : 10)
        .lore(lore)
        .build();
  }
}
