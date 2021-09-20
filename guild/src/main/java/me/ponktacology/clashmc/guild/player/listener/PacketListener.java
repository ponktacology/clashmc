package me.ponktacology.clashmc.guild.player.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.player.cps.CPSLimiter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.Optional;

public class PacketListener {

  public PacketListener( GuildPlayerCache playerCache, JavaPlugin plugin) {
    ProtocolLibrary.getProtocolManager()
        .addPacketListener(
            new PacketAdapter(plugin, PacketType.Play.Client.USE_ENTITY) {
              @Override
              public void onPacketReceiving( PacketEvent event) {
                Player player = event.getPlayer();
                PacketContainer packet = event.getPacket();
                Optional<GuildPlayer> guildPlayerOptional = playerCache.get(player);

                if (!guildPlayerOptional.isPresent()) {
                  return;
                }

                GuildPlayer guildPlayer = guildPlayerOptional.get();

                EnumWrappers.EntityUseAction type = packet.getEntityUseActions().read(0);

                if (type != EnumWrappers.EntityUseAction.ATTACK) return;

                CPSLimiter cpsLimiter = guildPlayer.getCpsLimiter();
                if (cpsLimiter.check()) {
                  cpsLimiter.resetLastFlag();

                  player.sendMessage(
                      Text.colored(
                          "&cPrzekroczyles limit cps &7(15)&c! &c&lTwoje hity zostaly zablokowane!"));
                  event.setCancelled(true);
                }
              }
            });
  }
}
