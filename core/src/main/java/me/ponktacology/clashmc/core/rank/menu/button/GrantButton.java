package me.ponktacology.clashmc.core.rank.menu.button;

import me.ponktacology.clashmc.api.util.Console;
import me.ponktacology.clashmc.api.util.TimeUtil;
import me.ponktacology.clashmc.api.util.Unknown;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.factory.CorePlayerFactory;
import me.ponktacology.clashmc.core.rank.grant.Grant;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GrantButton extends Button {

  private final Grant grant;
  private final CorePlayerFactory playerFactory;

  public GrantButton(Grant grant, CorePlayerFactory playerFactory) {
    this.grant = grant;
    this.playerFactory = playerFactory;
  }

  @Override
  public ItemStack getButtonItem(Player player) {
    List<String> lore = new ArrayList<>();

    String formattedIssuerName;
    if (grant.getAddedBy().equals(Console.UUID)) {
      formattedIssuerName = Console.FORMATTED_NAME;
    } else {
      Optional<CorePlayer> issuerPlayerOptional = playerFactory.load(grant.getAddedBy());

      formattedIssuerName =
          issuerPlayerOptional.map(CorePlayer::getFormattedName).orElse(Unknown.FORMATTED_NAME);
    }

    lore.add("&eAdministrator: &f" + formattedIssuerName);
    lore.add("&ePowód: &f" + grant.getReason());
    lore.add(
        "&eWygasa: &f"
            + (grant.hasExpired()
                ? "już wygasł"
                : grant.getFormattedExpireDate()));
    lore.add("&eData: &f" + TimeUtil.formatTimeMillisToDate(grant.getAddedOn()));

    if (grant.isRemoved()) {
      if (grant.getAddedBy().equals(Console.UUID)) {
        formattedIssuerName = Console.FORMATTED_NAME;
      } else {
        Optional<CorePlayer> issuerPlayerOptional = playerFactory.load(grant.getAddedBy());

        formattedIssuerName =
            issuerPlayerOptional.map(CorePlayer::getFormattedName).orElse(Unknown.FORMATTED_NAME);
      }

      lore.add(Text.SB_BAR);
      lore.add("&eAnulowany przez: &f" + formattedIssuerName);
      lore.add("&ePowód: &f" + grant.getRemoveReason());
      lore.add("&eData: &f" + TimeUtil.formatTimeMillisToDate(grant.getRemovedOn()));
    }

    return new ItemBuilder(Material.INK_SACK)
        .name("&e" + grant.getRankName())
        .durability(grant.isRemoved() ? 8 : 10)
        .lore(lore)
        .build();
  }
}
