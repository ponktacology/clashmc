package me.ponktacology.clashmc.guild.guild.permission;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.ponktacology.clashmc.guild.guild.GuildRole;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@ToString
public class GuildPermission implements Serializable {

  private final Map<Permissions, Boolean> permissions = new HashMap<>();

  public GuildPermission(GuildRole guildRole) {
    for (Permissions permissions : Permissions.values()) {
      if (guildRole.equals(GuildRole.LEADER)) {
        this.permissions.put(permissions, true);
      }

      switch (guildRole) {
        case LEADER:
          this.permissions.put(permissions, true);
          break;
        case MODERATOR:
          this.permissions.put(permissions, permissions.defaultValueModerator);
          break;
        default:
          this.permissions.put(permissions, permissions.defaultValueMember);
          break;
      }
    }
  }

  public void setPermission(Permissions permission, boolean value) {
    permissions.put(permission, value);
  }

  public boolean hasPermission(Permissions permission) {
    return permissions.computeIfAbsent(permission, Permissions::isDefaultValueMember);
  }

  public LinkedHashSet<Permissions> getPermissions() {
    return permissions.keySet().stream()
        .sorted()
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  public void togglePermission(Permissions permission) {
    boolean enabled = this.hasPermission(permission);
    this.setPermission(permission, !enabled);
  }

  @RequiredArgsConstructor
  @Getter
  public enum Permissions {
    BLOCK_BREAK("Niszczenie bloków", true, true),
    BLOCK_PLACE("Stawianie bloków", true, true),
    CHEST_ACCESS("Dostęp do skrzynek", true, true),
    PRIVATE_CHEST_CREATION("Możliwość tworzenia prywatnych skrzynek i piecyków", true, true),
    PRIVATE_CHEST_ACCESS("Dostęp do wszystkich prywatnych skrzynek i piecyków", false, true),
    INVITE_MEMBER("Zapraszanie graczy do gildii", false, true),
    KICK_MEMBER("Wyrzucanie graczy z gildii", false, true),
    INVITE_ALLY("Zawieranie sojuszy", false, true),
    REMOVE_ALLY("Zrywanie sojuszy", false, true),
    DECLARE_WAR("Wypowiadanie wojen", false, true),
    FRIENDLY_FIRE("Włączanie/Wyłączanie PvP w gildii", false, true),
    BASE_LOCATION("Ustawianie lokalizacji bazy", false, true),
    RANK_CHANGE("Zmienianie rang członków gildii", false, false),
    STONE_FARMER("Korzystanie z kopacza fosy", false, true),
    ENLARGE_GUILD("Powiększanie gildii", false, true),
    PERMISSION_ACCESS("Dostęp do zmiany pozwoleń", false, false);

    private final String formattedName;
    private final boolean defaultValueMember;
    private final boolean defaultValueModerator;
  }
}
