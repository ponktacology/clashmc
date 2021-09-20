package me.ponktacology.clashmc.guild.guild.updater;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.cache.GuildCache;
import me.ponktacology.clashmc.guild.guild.factory.GuildFactory;
import me.ponktacology.clashmc.guild.guild.updater.packet.PacketGuildRemove;
import me.ponktacology.clashmc.guild.guild.updater.packet.PacketGuildUpdate;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class GuildUpdater implements Updater {

  private final NetworkService networkService;

  private final DataService dataService;

  public void update(Guild guild) {
    guild.save();
    this.networkService.publish(new PacketGuildUpdate(guild.getTag()));
  }

  public void remove(Guild guild) {
    this.dataService.delete(guild);
    this.networkService.publish(new PacketGuildRemove(guild.getTag()));
  }

  @RequiredArgsConstructor
  public static class GuildUpdateListener implements PacketListener {

    private final GuildFactory guildFactory;
    private final GuildCache guildCache;
    private final TaskDispatcher taskDispatcher;

    @PacketHandler
    public void onGuildUpdate(PacketGuildUpdate packet) {
      Optional<Guild> guildOptional = this.guildFactory.load(packet.getGuild());

      if (!guildOptional.isPresent()) {
        log.info("Received guild update but guild not in database guild= " + packet.getGuild());
        return;
      }

      Guild guild = guildOptional.get();

      this.guildCache.add(guild);
    }

    @PacketHandler
    public void onGuildRemove(PacketGuildRemove packet) {
      this.guildCache.remove(packet.getGuild());
    }
  }
}
