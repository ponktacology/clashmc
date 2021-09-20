package me.ponktacology.clashmc.guild.player.incognito;

import com.mojang.authlib.GameProfile;
import lombok.Getter;
import lombok.Setter;
import me.ponktacology.clashmc.api.settings.PlayerSettings;
import me.ponktacology.clashmc.guild.util.RandomStringUtil;

import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Setter
public class IncognitoSettings implements PlayerSettings {

  private final AtomicBoolean enabled = new AtomicBoolean(false);

  private final transient String name = RandomStringUtil.get(8);

  private transient GameProfile cachedGameProfile;

  public boolean toggle() {
    this.enabled.set(!isEnabled());

    return isEnabled();
  }

  public boolean isEnabled() {
    return enabled.get();
  }
}
