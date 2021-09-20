package me.ponktacology.clashmc.guild.player.incognito.hologram;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.util.Pair;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.util.PacketUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSilverfish;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class IncognitoHologram {

  private final GuildPlayer guildPlayer;
  private final Map<UUID, Pair<Integer, Integer>> entityIds = Maps.newConcurrentMap();

  public void spawnForStaffMembersAndGuildMembers() {
    Set<Player> players = Sets.newHashSet();

    players.addAll(
        CorePlugin.INSTANCE.getPlayerCache().staffMembers().stream()
            .map(it -> it.getPlayer())
            .filter(Objects::nonNull)
            .collect(Collectors.toSet()));

    if (this.guildPlayer.hasGuild()) {
      players.addAll(
          this.guildPlayer.getGuild().get().onlineMembers().stream()
              .map(it -> it.getPlayer())
              .filter(Objects::nonNull)
              .collect(Collectors.toSet()));
    }

    for (Player player : players) {
      spawn(player);
    }
  }

  public void destroyForStaffMembersAndGuildMembers() {
    Set<Player> players = Sets.newHashSet();

    players.addAll(
        CorePlugin.INSTANCE.getPlayerCache().staffMembers().stream()
            .map(it -> it.getPlayer())
            .filter(Objects::nonNull)
            .collect(Collectors.toSet()));

    if (this.guildPlayer.hasGuild()) {
      players.addAll(
          this.guildPlayer.getGuild().get().onlineMembers().stream()
              .map(it -> it.getPlayer())
              .filter(Objects::nonNull)
              .collect(Collectors.toSet()));

      for (Player player : players) {
        destroy(player);
      }
    }
  }

  public void spawn(Player other) {
    Player incognitoPlayer = guildPlayer.getPlayer();

    if (other.equals(incognitoPlayer)) return;

    Location location = incognitoPlayer.getLocation();
    World world = ((CraftWorld) location.getWorld()).getHandle();
    CraftServer server = (CraftServer) Bukkit.getServer();

    EntityArmorStand entityArmorStand =
        new EntityArmorStand(world, location.getX(), location.getY() - 10, location.getZ());
    CraftArmorStand craftArmorStand = new CraftArmorStand(server, entityArmorStand);

    craftArmorStand.setCustomName(Text.colored("&b" + this.guildPlayer.getName()));
    craftArmorStand.setCustomNameVisible(true);
    craftArmorStand.setGravity(false);
    craftArmorStand.setVisible(false);
    craftArmorStand.setCanPickupItems(false);
    craftArmorStand.setMarker(true);

    EntitySilverfish entitySlime = new EntitySilverfish(world);
    entitySlime.setInvisible(true);

    CraftSilverfish craftSlime = new CraftSilverfish(server, entitySlime);
    craftSlime.setCanPickupItems(false);

    this.entityIds.put(
        other.getUniqueId(), new Pair<>(craftArmorStand.getEntityId(), craftSlime.getEntityId()));

    EntityPlayer entityPlayer = ((CraftPlayer) incognitoPlayer).getHandle();

    PacketUtil.sendPacket(
        other,
        new PacketPlayOutSpawnEntityLiving(craftSlime.getHandle()),
        new PacketPlayOutSpawnEntityLiving(craftArmorStand.getHandle()));

    CorePlugin.INSTANCE
        .getTaskDispatcher()
        .runLater(
            () ->
                PacketUtil.sendPacket(
                    other,
                    new PacketPlayOutAttachEntity(
                        0, craftArmorStand.getHandle(), craftSlime.getHandle()),
                    new PacketPlayOutAttachEntity(0, craftSlime.getHandle(), entityPlayer)),
            1L,
            TimeUnit.SECONDS);
  }

  public void destroy(Player other) {
    if (other.equals(guildPlayer.getPlayer())) return;

    Optional.ofNullable(this.entityIds.remove(other.getUniqueId()))
        .ifPresent(
            it -> {
              PacketUtil.sendPacket(
                  other, new PacketPlayOutEntityDestroy(it.getKey(), it.getValue()));
            });
  }
}
