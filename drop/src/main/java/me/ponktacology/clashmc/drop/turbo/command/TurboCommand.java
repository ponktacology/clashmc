package me.ponktacology.clashmc.drop.turbo.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.time.Time;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.drop.DropPermissions;
import me.ponktacology.clashmc.drop.player.DropPlayer;
import me.ponktacology.clashmc.drop.turbo.global.cache.GlobalTurboCache;
import me.ponktacology.clashmc.drop.turbo.global.updater.GlobalTurboUpdater;
import me.ponktacology.clashmc.drop.turbo.player.PlayerTurbo;
import me.ponktacology.clashmc.drop.turbo.player.updater.PlayerTurboUpdater;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Name;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.command.CommandSender;


@RequiredArgsConstructor
public class TurboCommand {


  private final GlobalTurboCache globalTurboCache;

  private final GlobalTurboUpdater globalTurboUpdater;

  private final PlayerTurboUpdater senderTurboUpdater;

  @Command(
      value = "turbo drop",
      description = "Daje graczowi turbo drop na określony czzas",
      async = true)
  @Permission(DropPermissions.DROP_MANAGE)
  public void turboDrop(
           @Sender CommandSender sender,
          @Name("player") DropPlayer dropPlayer,
           @Name("duration") Time time) {
    this.senderTurboUpdater.update(new PlayerTurbo(dropPlayer, true, time.getTimeStamp()));

    sender.sendMessage(Text.colored("&aPomyślnie nadano turbo drop graczowi."));
  }

  @Command(
      value = "turbo exp",
      description = "Daje graczowi turbo exp na określony czzas",
      async = true)
  @Permission(DropPermissions.DROP_MANAGE)
  public void turboExp(
           @Sender CommandSender sender,
          @Name("player") DropPlayer dropPlayer,
           @Name("duration") Time time) {
    this.senderTurboUpdater.update(new PlayerTurbo(dropPlayer, false, time.getTimeStamp()));

    sender.sendMessage(Text.colored("&aPomyślnie nadano turbo exp graczowi."));
  }

  @Command(
      value = "turbo global exp",
      description = "Daje turbo drop globalnie na określony czas",
      async = true)
  @Permission(DropPermissions.DROP_MANAGE)
  public void turboExpGlobal( @Sender CommandSender sender,  @Name("duration") Time time) {
    this.globalTurboCache.get().setTurboExpDuration(time.getTimeStamp());
    this.globalTurboUpdater.update();

    sender.sendMessage(Text.colored("&aPomyślnie nadano turbo exp globalnie."));
  }

  @Command(
      value = "turbo global drop",
      description = "Daje turbo drop globalnie na określony czas",
      async = true)
  @Permission(DropPermissions.DROP_MANAGE)
  public void turboDropGlobal( @Sender CommandSender sender,  @Name("duration") Time time) {
    this.globalTurboCache.get().setTurboDropDuration(time.getTimeStamp());
    this.globalTurboUpdater.update();

    sender.sendMessage(Text.colored("&aPomyślnie nadano turbo drop globalnie."));
  }
}
