package me.ponktacology.clashmc.sector.player.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.sector.api.player.info.PlayerInfo;
import me.ponktacology.clashmc.sector.api.player.inventory.Inventory;
import me.ponktacology.clashmc.sector.api.player.inventory.update.InventoryUpdate;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@UtilityClass
@Slf4j
public class PlayerUtil {

  public static void update(Player player, InventoryUpdate update) {
    PlayerInventory playerInventory = player.getInventory();
    Inventory inventory = update.getInventory();

    try {
      if (update.isUpdateInventory()) {
        playerInventory.setContents(
            ByteSerializer.deserializeItems(inventory.getInventoryContents()));

        playerInventory.setArmorContents(
            ByteSerializer.deserializeItems(inventory.getArmorContents()));
      }

      if (update.isUpdateEnderchest()) {
        player
            .getEnderChest()
            .setContents(ByteSerializer.deserializeItems(inventory.getEnderContents()));
      }

      player.updateInventory();
    } catch (Exception e) {
      e.printStackTrace();
      log.info(
          "Player "
              + player.getUniqueId().toString()
              + " ("
              + player.getName()
              + ") "
              + " inventory was corrupted.");
    }
  }

  public static PlayerInfo wrap(Player player) {
    PlayerInfo info = new PlayerInfo();

    info.inventory = wrapInventory(player);

    info.vehicleEntityType =
        player.getVehicle() == null ? null : player.getVehicle().getType().toString();

    info.x = player.getLocation().getBlockX();
    info.y = player.getLocation().getBlockY();
    info.z = player.getLocation().getBlockZ();
    info.yaw = player.getLocation().getYaw();
    info.pitch = player.getLocation().getPitch();
    info.world = player.getWorld().getName();

    info.allowFlight = player.getAllowFlight();
    info.displayName = player.getDisplayName();
    info.exhaustion = player.getExhaustion();
    info.experience = player.getExp();
    info.levels = player.getLevel();
    info.totalExperience = player.getTotalExperience();
    info.flying = player.isFlying();
    info.flySpeed = player.getFlySpeed();
    info.foodLevel = player.getFoodLevel();
    info.health = player.getHealth();
    info.healthScale = player.getHealthScale();

    CraftPlayer craftPlayer = (CraftPlayer) player;
    info.absorptionAmount = craftPlayer.getHandle().getAbsorptionHearts();

    info.sneaking = player.isSneaking();
    info.walkSpeed = player.getWalkSpeed();
    info.fallDistance = player.getFallDistance();
    info.fireTicks = player.getFireTicks();
    info.maximumAir = player.getMaximumAir();
    info.maximumAirNoDamageTicks = player.getMaximumNoDamageTicks();
    info.gameMode = player.getGameMode().name();
    info.sprinting = player.isSprinting();
    info.op = player.isOp();
    info.canPickupItems = player.getCanPickupItems();
    info.noDamageTicks = player.getNoDamageTicks();
    info.heldItemSlot = player.getInventory().getHeldItemSlot();

    PlayerInfo.SerializablePotionEffect[] effects =
        new PlayerInfo.SerializablePotionEffect[player.getActivePotionEffects().size()];
    int i = 0;
    for (PotionEffect effect : player.getActivePotionEffects()) {
      PlayerInfo.SerializablePotionEffect potionEffect = new PlayerInfo.SerializablePotionEffect();

      potionEffect.type = effect.getType().toString();
      potionEffect.amplifier = effect.getAmplifier();
      potionEffect.duration = effect.getDuration();

      effects[i] = potionEffect;
      i++;
    }

    info.potionEffects = effects;

    return info;
  }

  public static void unwrap(Player player, PlayerInfo info) {
    try {
      Inventory inventory = info.inventory;

      if (inventory.getInventoryContents() != null) {
        player
            .getInventory()
            .setContents(ByteSerializer.deserializeItems(inventory.getInventoryContents()));
      } else player.getInventory().clear();

      if (inventory.getArmorContents() != null) {
        player
            .getInventory()
            .setArmorContents(ByteSerializer.deserializeItems(inventory.getArmorContents()));
      } else player.getInventory().setArmorContents(new ItemStack[] {null, null, null, null});

      if (inventory.getEnderContents() != null) {
        player
            .getEnderChest()
            .setContents(ByteSerializer.deserializeItems(inventory.getEnderContents()));
      } else player.getEnderChest().clear();

      player.updateInventory();
    } catch (Exception e) {
      e.printStackTrace();
      log.info(
          "Player "
              + player.getUniqueId().toString()
              + " ("
              + player.getName()
              + ") "
              + " inventory was corrupted.");
    }

    if (info.vehicleEntityType != null) {
      EntityType entityType = EntityType.valueOf(info.vehicleEntityType);
      Entity vehicle = player.getWorld().spawnEntity(player.getLocation(), entityType);
      vehicle.setPassenger(player);
    }

    Location location =
        new Location(Bukkit.getWorld(info.world), info.x, info.y, info.z, info.yaw, info.pitch);

    log.info("TELEPORTING TO " + location.toString());
    player.teleport(location);
    player.setBedSpawnLocation(location, true);
    player.setAllowFlight(info.allowFlight);
    player.setDisplayName(info.displayName);
    player.setExhaustion(info.exhaustion);
    player.setExp(info.experience);
    player.setLevel(info.levels);
    player.setTotalExperience(info.totalExperience);
    player.setFlying(info.flying);
    player.setFlySpeed(info.flySpeed);
    player.setFoodLevel(info.foodLevel);
    player.setHealth(info.health);
    player.setHealthScale(info.healthScale);
    player.setSneaking(info.sneaking);
    player.setWalkSpeed(info.walkSpeed);
    player.setFallDistance(info.fallDistance);
    player.setFireTicks(info.fireTicks);
    player.setMaximumAir(info.maximumAir);
    player.setMaximumNoDamageTicks(info.maximumAirNoDamageTicks);
    player.setGameMode(GameMode.valueOf(info.gameMode));
    player.setSprinting(info.sprinting);
    player.setOp(info.op);
    player.setCanPickupItems(info.canPickupItems);
    player.setNoDamageTicks(info.noDamageTicks);
    player.getInventory().setHeldItemSlot(info.heldItemSlot);

    if (info.potionEffects == null) {
      return;
    }

    player.getActivePotionEffects().forEach(it -> player.removePotionEffect(it.getType()));

    for (PlayerInfo.SerializablePotionEffect effect : info.potionEffects) {
      String[] args = effect.type.split(" ");

      if (args.length < 2) continue;

      PotionEffectType type = PotionEffectType.getByName(args[1].replace("]", ""));

      if (type == null) {
        continue;
      }

      PotionEffect potionEffect = new PotionEffect(type, effect.duration, effect.amplifier);
      player.addPotionEffect(potionEffect, true);
    }

    CraftPlayer craftPlayer = (CraftPlayer) player;

    craftPlayer.getHandle().setAbsorptionHearts(info.absorptionAmount);
  }

  public static Inventory wrapInventory(Player player) {
    PlayerInventory playerInventory = player.getInventory();
    Inventory inventory = new Inventory();

    inventory.setArmorContents(ByteSerializer.serializeItems(playerInventory.getArmorContents()));
    inventory.setInventoryContents(ByteSerializer.serializeItems(playerInventory.getContents()));
    inventory.setEnderContents(ByteSerializer.serializeItems(player.getEnderChest().getContents()));

    return inventory;
  }
}
