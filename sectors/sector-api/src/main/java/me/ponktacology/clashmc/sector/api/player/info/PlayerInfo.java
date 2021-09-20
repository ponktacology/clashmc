package me.ponktacology.clashmc.sector.api.player.info;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.ponktacology.clashmc.sector.api.player.inventory.Inventory;



import java.io.Serializable;

@ToString(callSuper = true)
public class PlayerInfo {

  public Inventory inventory;

  public int heldItemSlot;

  public String world;
  public double x;
  public double y;
  public double z;
  public float yaw;
  public float pitch;

  public String displayName;

  public int fireTicks;
  public int noDamageTicks;

  public String gameMode;

  public int maximumAir;
  public int maximumAirNoDamageTicks;
  public int remainingAir;

  public boolean op;

  public float fallDistance;

  public int foodLevel;
  public double health;
  public double healthScale;

  public boolean sneaking;
  public boolean sprinting;

  public float experience;
  public int levels;
  public int totalExperience;

  public float exhaustion;
  public float saturation;
  public float absorptionAmount;

  public boolean allowFlight;
  public boolean flying;
  public boolean canPickupItems;
  public float flySpeed;
  public float walkSpeed;

  
  public String vehicleEntityType;

  public SerializablePotionEffect[] potionEffects;

  @AllArgsConstructor
  @NoArgsConstructor
  public static class SerializablePotionEffect implements Serializable {

    public String type;
    public int duration;
    public int amplifier;


    @Override
    public String toString() {
      return "SerializablePotionEffect{"
          + "type='"
          + type
          + '\''
          + ", duration="
          + duration
          + ", amplifier="
          + amplifier
          + '}';
    }
  }
}
