package me.ponktacology.clashmc.core.chat.global.broadcast.announcer;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.announcer.Announcer;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.core.blazingpack.bar.BarColor;
import me.ponktacology.clashmc.core.blazingpack.bar.BarStyle;
import me.ponktacology.clashmc.core.blazingpack.bar.BossBar;
import me.ponktacology.clashmc.core.blazingpack.bar.manager.BarManager;
import me.ponktacology.clashmc.core.chat.global.broadcast.Broadcast;
import me.ponktacology.clashmc.core.chat.global.broadcast.announcer.packet.PacketBroadcastAnnounce;
import me.ponktacology.clashmc.core.util.ActionBarUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.core.util.TitleUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class BroadcastAnnouncer implements Announcer<Broadcast, PacketBroadcastAnnounce> {

  private final BarManager barManager;
  private final NetworkService networkService;
  private final TaskDispatcher taskDispatcher;

  @Override
  public void announce( Broadcast announce) {
    this.networkService.publish(
        new PacketBroadcastAnnounce(
            announce.getMessage(),
            announce.getType(),
            announce.getFadeIn(),
            announce.getDuration(),
            announce.getFadeOut()));
  }

  @PacketHandler
  @Override
  public void handle( PacketBroadcastAnnounce packet) {
    switch (packet.getType()) {
      case CHAT:
        {
          Bukkit.getServer().broadcastMessage(Text.colored(packet.getMessage()));
          break;
        }
      case TITLE:
        {
          TitleUtil.sendTitleAndSubtitle(
              Text.colored(packet.getMessage()),
              "",
              packet.getFadeIn(),
              packet.getDuration(),
              packet.getFadeOut());
          break;
        }
      case SUB_TITLE:
        {
          TitleUtil.sendTitleAndSubtitle(
              "",
              Text.colored(packet.getMessage()),
              packet.getFadeIn(),
              packet.getDuration(),
              packet.getFadeOut());
          break;
        }
      case BOSSBAR:
        {
          BossBar bar =
              new BossBar(
                  UUID.randomUUID(),
                  BarStyle.SOLID,
                  BarColor.WHITE,
                  TextComponent.fromLegacyText(Text.colored(packet.getMessage())),
                  1F);

          for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            this.barManager.update(player, bar);
          }

          this.taskDispatcher.runLaterAsync(
              () -> {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                  this.barManager.removeBar(player, bar);
                }
              },
              packet.getDuration() * 50L,
              TimeUnit.MILLISECONDS);

          break;
        }
      case ACTION_BAR:
        {
          for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            ActionBarUtil.sendActionBarMessage(
                player,
                Text.colored(packet.getMessage()),
                packet.getDuration());
          }
          break;
        }
    }
  }
}
