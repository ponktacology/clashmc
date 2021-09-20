package me.ponktacology.clashmc.guild.guild;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;

@RequiredArgsConstructor
@Getter
public enum GuildRole {
  LEADER("Lider", Material.GOLD_HELMET, 10),
  MODERATOR("Zastępca", Material.IRON_HELMET, 9),
  TRUSTED("Zaufany", Material.CHAINMAIL_HELMET, 8),
  DEFAULT("Członek", Material.CHAINMAIL_HELMET, 7),
  NEW("Rekrut", Material.LEATHER_HELMET, 6),
  OTHER("Inna", Material.LEATHER_HELMET, 5);

  private final String formattedName;
  private final Material icon;
  private final int power;

  public boolean canManage(GuildRole role) {
    return role != LEADER && (this == LEADER || this.getPower() > role.getPower());
  }
}
