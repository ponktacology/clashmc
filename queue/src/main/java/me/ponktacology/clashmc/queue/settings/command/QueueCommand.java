package me.ponktacology.clashmc.queue.settings.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.util.StyleUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.queue.QueuePermissions;
import me.ponktacology.clashmc.queue.entry.cache.QueueEntryCache;
import me.ponktacology.clashmc.queue.settings.QueueSettings;
import me.ponktacology.clashmc.queue.settings.cache.QueueSettingsCache;
import me.ponktacology.clashmc.queue.settings.updater.QueueSettingsUpdater;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Name;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class QueueCommand {

  private final QueueEntryCache entryCache;

  private final QueueSettingsCache settingsCache;

  private final QueueSettingsUpdater settingsUpdater;

  @Command(value = "queuedebug")
  @Permission(QueuePermissions.QUEUE_STATE)
  public void debug(@Sender CommandSender sender) {
    sender.sendMessage(Bukkit.getServer().getOnlinePlayers().size() + "");
    sender.sendMessage(entryCache.size() + " ");
  }

  @Command(value = "queue state", description = "Włącza/wyłącza kolejkę", async = true)
  @Permission(QueuePermissions.QUEUE_STATE)
  public void state(@Sender CommandSender sender, @Name("enabled") boolean enabled) {
    QueueSettings queueSettings = this.settingsCache.get();
    queueSettings.setEnabled(enabled);
    this.settingsUpdater.update(queueSettings);

    sender.sendMessage(
        Text.colored("&aPomyślnie " + StyleUtil.state(enabled) + " &awysyłanie graczy z kolejki."));
  }

  @Command(
      value = "queue maxplayers",
      description = "Ustawia maksymalną ilość graczy",
      async = true)
  @Permission(QueuePermissions.QUEUE_STATE)
  public void state(@Sender CommandSender sender, @Name("amount") int amount) {
    QueueSettings queueSettings = this.settingsCache.get();
    queueSettings.setMaxPlayers(amount);
    this.settingsUpdater.update(queueSettings);

    sender.sendMessage(Text.colored("&aPomyślnie ustawiono maksymalną ilość graczy na serwerze."));
  }

  @Command(
      value = "queue advanced",
      description = "Włącza/wyłącza zaawansowany tryb kolejki",
      async = true)
  @Permission(QueuePermissions.QUEUE_STATE)
  public void advanced(@Sender CommandSender sender, @Name("enabled") boolean enabled) {
    QueueSettings queueSettings = this.settingsCache.get();
    queueSettings.setAdvanced(enabled);
    this.settingsUpdater.update(queueSettings);

    sender.sendMessage(
        Text.colored("&aPomyślnie " + StyleUtil.state(enabled) + " &azaawansowany tryb kolejki."));
  }
}
