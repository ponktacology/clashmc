package me.ponktacology.clashmc.backup;

import lombok.Getter;
import me.ponktacology.clashmc.backup.player.BackupPlayer;
import me.ponktacology.clashmc.backup.player.adapter.BackupPlayerParameterAdapter;
import me.ponktacology.clashmc.backup.player.backup.updater.PlayerBackupUpdater;
import me.ponktacology.clashmc.backup.player.backup.updater.packet.PacketPlayerBackupUpdate;
import me.ponktacology.clashmc.backup.player.command.BackupCommand;
import me.ponktacology.clashmc.backup.player.factory.BackupPlayerFactory;
import me.ponktacology.clashmc.backup.player.listener.PlayerRankDeathListener;
import me.ponktacology.clashmc.backup.player.task.PlayerBackupTask;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.util.PluginUtil;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.loader.plugin.BukkitPlugin;
import me.ponktacology.clashmc.sector.SectorPlugin;
import me.vaperion.blade.Blade;
import me.vaperion.blade.command.container.impl.BukkitCommandContainer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

@Getter
public enum BackupPlugin implements BukkitPlugin {
  INSTANCE;

  private final BackupPlayerFactory playerFactory = new BackupPlayerFactory();
  private final PlayerBackupUpdater backupUpdater =
      new PlayerBackupUpdater(
          CorePlugin.INSTANCE.getNetworkService(),
          CorePlugin.INSTANCE.getPlayerCache(),
          GuildPlugin.INSTANCE.getPlayerCache(),
          SectorPlugin.INSTANCE.getInventoryUpdater());

  @Override
  public void enable(JavaPlugin plugin) {
    PluginUtil.registerListener(
        new PlayerRankDeathListener(this.playerFactory, CorePlugin.INSTANCE.getTaskDispatcher()));

    this.registerPlayerBackups();

    Blade corePluginBlade = CorePlugin.INSTANCE.getBlade();
    Blade.of()
        .fallbackPrefix("backup")
        .containerCreator(BukkitCommandContainer.CREATOR)
        .bindings(corePluginBlade.getBindings())
        .asyncExecutor(corePluginBlade.getAsyncExecutor())
        .customProviderMap(corePluginBlade.getCustomProviderMap())
        .tabCompleter(corePluginBlade.getTabCompleter())
        .helpGenerator(corePluginBlade.getHelpGenerator())
        .bind(BackupPlayer.class, new BackupPlayerParameterAdapter(this.playerFactory))
        .build()
        .register(new BackupCommand());


  }

  private void registerPlayerBackups() {
    CorePlugin corePlugin = CorePlugin.INSTANCE;

    corePlugin.getPacketCache().add(PacketPlayerBackupUpdate.class);
    corePlugin.getNetworkService().subscribe(this.backupUpdater);

    corePlugin
        .getTaskDispatcher()
        .runTimerAsync(new PlayerBackupTask(this.playerFactory), 15L, TimeUnit.MINUTES);
  }
}
