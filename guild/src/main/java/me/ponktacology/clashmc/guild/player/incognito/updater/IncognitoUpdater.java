package me.ponktacology.clashmc.guild.player.incognito.updater;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.core.util.PacketUtil;
import me.ponktacology.clashmc.core.util.reflection.FieldAccessor;
import me.ponktacology.clashmc.core.util.reflection.Reflection;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class IncognitoUpdater implements Updater {

  private static final String SKIN =
      "ewogICJ0aW1lc3RhbXAiIDogMTYxMTA4MjIzMzAyMSwKICAicHJvZmlsZUlkIiA6ICIwNjlhNzlmNDQ0ZTk0NzI2YTViZWZjYTkwZTM4YWFmNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJOb3RjaCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yOTIwMDlhNDkyNWI1OGYwMmM3N2RhZGMzZWNlZjA3ZWE0Yzc0NzJmNjRlMGZkYzMyY2U1NTIyNDg5MzYyNjgwIgogICAgfQogIH0KfQ==";
  private static final String SIGNATURE =
      "hctAR8Nd3h4CmMOog1kSzgJ8yhkc/u5MgRd72gU7xZzIgIkVxWfdkpI+cSwmRW60hsrs7iJUoj9KPHdYgS5aOUuZ+l/zUg9s/i2sR/xDKi3BjiUhDA+9MuLKLKcLXFN7lUjUHvKQlilS53Exh4w6DQbJP+uru3nThB2XfJHyziRp8Zymyy6C+zcQZMA92jo+WW8lZp3fPrNZj0o7G6bdhu+0Ai8gBxqKb5wgMJdsTr7zcopr1G/yVyLYnC7FCLLtGSQwe45ltt+Hi+kESSagTiQePHSbLojDnV2hPO85PvrU2hTiwgIHH+HuW96BhYOlq/nI9a+96D2E55f5h49zSDU84LPKuuKrjBDXxrvag9BXa4N8ESNktCUzbqhjAUqvCIcQcdImFJ3/UNRyyx1KELeybunDBJbRGkfNmu8yWczo2BurSC50wGnXZqwgwNAQLVBzntiR+kx5ZUXeRSL3LrSwQJanTEm5AH9bmGro5A0gdQiY3c2noGWmUBBFqYc3IP2AmhGR9ssNqH7LW2/u3HBsi5Ti3y62PqApPCuMaPCfONcMDM+s9X+5zW7SDXzILQ0G6QjO/KYP8kBnA+R3o/W77y/SERr7HuOyW5hlFuwxEbaKp3KS5CmCpbhfdJ9bHPE+Q8ErkQpI48wyJa7tmUihJPw6bp5CBAu1VDnGHfw=";

  private final TaskDispatcher taskDispatcher;

  public void update(GuildPlayer guildPlayer) {
    changeNameAndSkin(guildPlayer);
  }

  public void changeNameAndSkin(GuildPlayer guildPlayer) {
    Player player = guildPlayer.getPlayer();

    if (player == null) return;

    CraftPlayer craftPlayer = (CraftPlayer) player;
    EntityPlayer entityPlayer = craftPlayer.getHandle();
    AtomicReference<GameProfile> gameProfile = new AtomicReference<>();

    if (guildPlayer.isIncognitoEnabled()) {
      guildPlayer.setCachedIncognitoGameProfile(craftPlayer.getProfile());

      String name = guildPlayer.getIncognitoName();
      gameProfile.set(
          new GameProfile(player.getUniqueId(), ChatColor.MAGIC + name + ChatColor.RESET));
      gameProfile.get().getProperties().put("textures", new Property("textures", SKIN, SIGNATURE));
      guildPlayer.spawnIncognitoHologramForStaffMembers();
    } else {
      guildPlayer.getCachedIncognitoGameProfile().ifPresent(gameProfile::set);
      guildPlayer.destroyIncognitoHologramForStaffMembers();
    }

    FieldAccessor<GameProfile> bH =
        Reflection.getField(EntityPlayer.class, "bH", GameProfile.class);

    GameProfile profile = gameProfile.get();

    if (profile != null) {
      bH.set(entityPlayer, profile);
    }

    PacketPlayOutPlayerInfo packetPlayOutPlayerInfo =
        new PacketPlayOutPlayerInfo(
            PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
    PacketPlayOutEntityDestroy packetPlayOutEntityDestroy =
        new PacketPlayOutEntityDestroy(craftPlayer.getEntityId());

    PacketUtil.sendPacketExcept(player, packetPlayOutPlayerInfo, packetPlayOutEntityDestroy);

    this.taskDispatcher.runLaterAsync(
        () -> {
          PacketPlayOutPlayerInfo packetPlayOutPlayerInfo1 =
              new PacketPlayOutPlayerInfo(
                  PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, craftPlayer.getHandle());

          PacketUtil.sendPacketExcept(
              player,
              packetPlayOutPlayerInfo1,
              new PacketPlayOutNamedEntitySpawn(craftPlayer.getHandle()));
        },
        100,
        TimeUnit.MILLISECONDS);
  }

  public void sendHologram(GuildPlayer guildPlayer, GuildPlayer incognitoGuildPlayer) {
    incognitoGuildPlayer.spawnIncognitoHologram(guildPlayer);
  }
}
