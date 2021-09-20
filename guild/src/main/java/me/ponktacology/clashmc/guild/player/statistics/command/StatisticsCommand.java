package me.ponktacology.clashmc.guild.player.statistics.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Name;
import me.vaperion.blade.command.annotation.Optional;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.command.CommandSender;


import java.util.Arrays;

@RequiredArgsConstructor
public class StatisticsCommand {


  private final GuildPlayerCache playerCache;

  @Command(value = {"gracz", "ranking", "rank", "statystyki", "staty"}, async = true)
  public void execute(
           @Sender CommandSender sender,  @Optional("self") @Name("player") GuildPlayer guildPlayer) {

    Arrays.asList(
            "&eStatystyki gracza &f" + guildPlayer.getName(),
            " &f* &7Ranking: &f" + guildPlayer.getRank(),
            " &f* &7Pozycja: &f" + this.playerCache.getPosition(guildPlayer),
            " &f* &7Zabójstwa: &f" + guildPlayer.getKills(),
            " &f* &7Asysty: &f" + guildPlayer.getAssists(),
            " &f* &7Śmierci: &f" + guildPlayer.getDeaths(),
            " &f* &7K/D: &f" + guildPlayer.getKD(),
            "&aWięcej informacji o graczu znajdziesz na stronie:",
            " &ehttps://clashmc.pl/p/" + guildPlayer.getName())
        .forEach(it -> sender.sendMessage(Text.colored(it)));
  }
}
