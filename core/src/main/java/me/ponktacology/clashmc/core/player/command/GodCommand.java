package me.ponktacology.clashmc.core.player.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.CorePermissions;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.StyleUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.vaperion.blade.command.annotation.*;
import org.bukkit.entity.Player;


@RequiredArgsConstructor
public class GodCommand {


  private final CorePlayerCache playerCache;

  @Command(
      value = {"god"},
      description = "Włącza/Wyłącza goda",
      async = true)
  @Permission(CorePermissions.GOD)
  public void execute( @Sender Player sender,  @Name("player") @Optional("self") Player player) {
    CorePlayer corePlayer = this.playerCache.getOrKick(player);

    corePlayer.toggleGod();
    corePlayer.save();

    sender.sendMessage(
        Text.colored("&aPomyślnie " + StyleUtil.state(corePlayer.isGod()) + " &agoda."));
  }

  @Command(value = {"gtest", "godtest"})
  @Permission(CorePermissions.DEV)
  public void vanishTest( @Sender Player sender,  @Name("player") @Optional("self") Player player) {
    CorePlayer corePlayer = this.playerCache.getOrKick(player);

    sender.sendMessage(corePlayer.isGod() + "");
  }
}
