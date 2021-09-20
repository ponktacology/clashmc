package me.ponktacology.clashmc.core.player.command;

import me.ponktacology.clashmc.core.util.Text;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.command.CommandSender;


import java.util.Arrays;

public class DonorCommand {

  @Command(value = "vip")
  public void vip( @Sender CommandSender sender) {
    Arrays.asList(
            "&8- &eCzas trwania&8: &7NA EDYCJE&8!",
            "&8- &eKoszt rangi&8: &710PLN&8.",
            "&8- &eMoże wchodzić na pełny serwer&8.",
            "&8- &eDostęp do komendy /repair&8.",
            "&8- &ePrzenośny enderchest pod komendą /ec&8.",
            "&8- &eZwiększony drop o 15%&8.",
            "&8- &eKoszt założenia gildii zmniejszony o 25%&8.",
            "&8- &eDostęp do komendy /kit vip&8.",
            "&8- &eDarmowa komenda /tpa&8.",
            "&8- &eAby zakupić rangę &6VIP &ewejdź na naszą stronę&8: &7www.clashmc.pl&8.")
        .forEach(it -> sender.sendMessage(Text.colored(it)));
  }

  @Command(value = "svip")
  public void svip( @Sender CommandSender sender) {
    Arrays.asList(
            "&8- &eCzas trwania&8: &7NA EDYCJE&8!",
            "&8- &eKoszt rangi&8: &720PLN&8.",
            "&8- &eMoże wchodzić na pełny serwer&8.",
            "&8- &eDostęp do komendy /repair all&8.",
            "&8- &ePrzenośny enderchest pod komendą /ec&8.",
            "&8- &eZwiększony drop o 30%&8.",
            "&8- &eKoszt założenia gildii zmniejszony o 50%&8.",
            "&8- &eDostęp do komendy /kit svip&8.",
            "&8- &eDarmowa komenda /tpa&8.",
            "&8- &eAby zakupić rangę &eSVIP &ewejdź na naszą stronę&8: &7www.clashmc.pl&8.")
        .forEach(it -> sender.sendMessage(Text.colored(it)));
  }

  @Command(value = "yt")
  public void yt( @Sender CommandSender sender) {
    Arrays.asList(
            "&8- &eCzas trwania&8: &7NA ZAWSZE&8!",
            "&8- &eWymagania&8: &73000 SUBOW&8.",
            "&8- &eMoże wchodzić na pełny serwer&8.",
            "&8- &eDostęp do komendy /repair&8.",
            "&8- &ePrzenośny enderchest pod komendą /ec&8.",
            "&8- &eZwiększony drop o 30%&8.",
            "&8- &eKoszt założenia gildii zmniejszony o 50%&8.",
            "&8- &eDostęp do komendy /kit yt&8.",
            "&8- &eDarmowa komenda /tpa&8.",
            "&8- &eAby otrzymać rangę &4Y&fT &eskontaktuj się z administracją na&8: &7ts.clashmc.pl&8.")
        .forEach(it -> sender.sendMessage(Text.colored(it)));
  }
}
