package me.ponktacology.clashmc.guild.guild.permission;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.ponktacology.clashmc.guild.guild.GuildRole;

import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode
@ToString
public class GuildPermissions {

  private final Map<GuildRole, GuildPermission> guildPermissionMap = new HashMap<>();

  public GuildPermissions() {
    for (GuildRole role : GuildRole.values()) {
      guildPermissionMap.put(role, new GuildPermission(role));
    }
  }

  public GuildPermission getPermission(GuildRole role) {
    return guildPermissionMap.computeIfAbsent(role, GuildPermission::new);
  }

  public boolean hasPermission(GuildRole role, GuildPermission.Permissions permission) {
    return getPermission(role).hasPermission(permission);
  }

  public void setPermission(GuildRole role, GuildPermission.Permissions permission, boolean value) {
    getPermission(role).setPermission(permission, value);
  }

  public void togglePermission(GuildRole role, GuildPermission.Permissions permission) {
    setPermission(role, permission, !hasPermission(role, permission));
  }
}
