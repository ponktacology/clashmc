package me.ponktacology.clashmc.guild.player.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class ResetRankCommand {

  private final GuildPlayerCache playerCache;

  @Command(value = "resetujranking", description = "Resetuje wszystkie statystyki", async = true)
  public void execute(@Sender Player sender) {
    GuildPlayer guildPlayer = this.playerCache.getOrKick(sender);

    guildPlayer.resetStatistics();
    guildPlayer.save();
    sender.sendMessage(Text.colored("&aPomyślnie zresetowano ranking."));
  }
}
