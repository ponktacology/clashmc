package me.ponktacology.clashmc.core.player.command;

import me.ponktacology.clashmc.core.util.Text;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.command.CommandSender;


import java.util.Arrays;

public class HelpCommand {

  @Command(value = {"pomoc", "help", "ver", "?"})
  public void help( @Sender CommandSender sender) {
    Arrays.asList(
            "&e/vip &8- &7Informacje o randze &6VIP&8.",
            "&e/svip &8- &7Informacje o randze &eSVIP&8.",
            "&e/yt &8- &7Informacje o randze &4Y&fT&8.",
            "&e/resetujranking &8- &7Resetuje ranking oraz statystyki do stanu początkowego&8.",
            "&e/craftingi &8- &7Przydatne craftingi na serwerze&8.",
            "&e/drop &8- &7Informacje o dropie na serwerze&8.",
            "&e/kit &8- &7Zestawy dostępne na serwerze&8.",
            "&e/g &8- &7Wszystkie komendy dotyczące gildii&8.",
            "&e/cc &8- &7Możliwość modyfikowania swojego chatu&8.",
            "&e/cobblex &8- &7Komenda na wytworzenie cobblex&8.")
        .forEach(it -> sender.sendMessage(Text.colored(it)));
  }
}
