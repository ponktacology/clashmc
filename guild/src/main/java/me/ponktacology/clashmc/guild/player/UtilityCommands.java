package me.ponktacology.clashmc.guild.player;

import io.netty.buffer.Unpooled;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.CorePermissions;
import me.ponktacology.clashmc.core.blazingpack.cuboid.CuboidHelper;
import me.ponktacology.clashmc.core.blazingpack.cuboid.CustomPayload;
import me.ponktacology.clashmc.core.blazingpack.cuboid.MessagedRectangle;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.api.sector.SectorType;
import me.ponktacology.clashmc.sector.api.sector.cache.SectorCache;
import me.ponktacology.clashmc.sector.player.transfer.PlayerTransferUpdater;
import me.ponktacology.clashmc.sector.sector.util.RegionUtil;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Name;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;


import java.util.Optional;

@RequiredArgsConstructor
public class UtilityCommands {


  private final SectorCache sectorCache;

  private final PlayerTransferUpdater transferUpdater;

  @Command(value = "rect")
  @Permission(CorePermissions.DEV)
  public void execute( @Sender Player player, @Name("width") int width) {
    Location location = player.getLocation();
    MessagedRectangle rectangle =
        new MessagedRectangle(
            "siema eniu", location.getBlockX(), location.getBlockZ(), width, width, false, false);
    CustomPayload payload = CuboidHelper.addSomeCuboids(rectangle);
    PacketPlayOutCustomPayload packet =
        new PacketPlayOutCustomPayload(
            payload.getChannel(),
            new PacketDataSerializer(Unpooled.wrappedBuffer(payload.getData())));

    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    player.sendMessage(Text.colored("&aWysłano"));
  }

  @Command(value = "randomtp")
  @Permission(CorePermissions.DEV)
  public void randomTp(@Sender Player player) {
    Optional<Sector> sectorOptional = this.sectorCache.getLeastCrowded(SectorType.DEFAULT);

    if (!sectorOptional.isPresent()) {
      return;
    }

    Sector sector = sectorOptional.get();

    this.transferUpdater.update(player, sector, RegionUtil.getRandomLocationInSector(sector));
  }
}
