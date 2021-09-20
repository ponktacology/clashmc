package me.ponktacology.clashmc.auth.player;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.auth.util.BCrypt;
import me.ponktacology.clashmc.core.player.BukkitPlayerWrapper;
import me.ponktacology.clashmc.core.util.PlayerUtil;
import org.bukkit.entity.Player;


import java.util.UUID;

@Slf4j
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(collection = "players", database = "auth")
public class AuthPlayer extends BukkitPlayerWrapper {

  private String password;
  private String rememberedIp;
  private boolean registered;
  private boolean remember;

  private transient boolean loggedIn;

  public AuthPlayer(UUID uuid, String name) {
    super(uuid, name);
  }

  public void resetPassword() {
    this.password = null;
    this.rememberedIp = null;
    this.registered = false;
    this.remember = false;
  }

  public void setLoggedIn(boolean loggedIn) {
    this.loggedIn = loggedIn;
    this.remember();
  }

  public boolean isLoggedIn() {
    return this.registered && (this.loggedIn || this.isRemembered());
  }

  public boolean isPasswordCorrect(String password) {
    return BCrypt.checkpw(password, this.password);
  }

  public void register(String password) {
    this.registered = true;
    this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    this.loggedIn = true;

    this.remember();
  }

  public void remember() {
    Player player = this.getPlayer();

    if (player == null) {
      return;
    }

    this.remember = true;
    this.rememberedIp = PlayerUtil.getAddress(player);
  }

  private boolean isRemembered() {
    Player player = this.getPlayer();

    if (player == null) {
      return false;
    }

    return this.remember
        && this.rememberedIp != null
        && this.rememberedIp.equals(PlayerUtil.getAddress(player));
  }
}
