package me.ponktacology.clashmc.guild.guild.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.CorePermissions;
import me.ponktacology.clashmc.core.util.StyleUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.GuildConstants;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.player.incognito.updater.IncognitoUpdater;
import me.ponktacology.clashmc.guild.util.SchematicUtil;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class IncognitoCommand {

  private final GuildPlayerCache playerCache;
  private final IncognitoUpdater incognitoUpdater;

  @Command(value = "incognito")
  public void execute(@Sender Player sender) {
    GuildPlayer guildPlayer = this.playerCache.getOrKick(sender);

    sender.sendMessage(
        Text.colored(
            "&aPomyślnie " + StyleUtil.state(guildPlayer.toggleIncognito()) + " &aincognito."));

    this.incognitoUpdater.update(guildPlayer);
  }

  @Command(value = "paste")
  @Permission(CorePermissions.DEV)
  public void test(@Sender Player sender) {
    SchematicUtil.pasteSchematic(
        GuildConstants.CENTER_SCHEMATIC, sender.getLocation().getBlock().getLocation(), false);
  }
}
