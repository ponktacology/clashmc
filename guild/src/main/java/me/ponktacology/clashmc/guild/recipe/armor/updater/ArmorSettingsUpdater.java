package me.ponktacology.clashmc.guild.recipe.armor.updater;

import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.settings.updater.SettingsUpdater;
import me.ponktacology.clashmc.core.util.ItemUtil;
import me.ponktacology.clashmc.guild.recipe.armor.ArmorRecipeCache;
import me.ponktacology.clashmc.guild.recipe.armor.ArmorSettings;
import me.ponktacology.clashmc.guild.recipe.armor.cache.ArmorSettingsCache;
import me.ponktacology.clashmc.guild.recipe.armor.factory.ArmorSettingsFactory;
import me.ponktacology.clashmc.guild.recipe.armor.updater.packet.PacketArmorSettingsState;
import me.ponktacology.clashmc.guild.recipe.armor.updater.packet.PacketArmorSettingsUpdate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

public class ArmorSettingsUpdater
    extends SettingsUpdater<ArmorSettings, PacketArmorSettingsUpdate> {

  private final ArmorRecipeCache armorRecipeCache;

  public ArmorSettingsUpdater(
      DataService dataService,
      NetworkService networkService,
      ArmorSettingsCache settingsCache,
      ArmorSettingsFactory settingsFactory,
      ArmorRecipeCache armorRecipeCache) {
    super(
        dataService,
        networkService,
        PacketArmorSettingsUpdate.class,
        settingsCache,
        settingsFactory);
    this.armorRecipeCache = armorRecipeCache;
  }

  public void toggle(Material material) {
    ArmorSettings armorSettings = this.settingsCache.get();
    boolean enabled = armorSettings.enabled(material);

    if (enabled) {
      disable(material);
    } else enable(material);
  }

  public void update(Material material) {
    ArmorSettings armorSettings = this.settingsCache.get();

    armorSettings.save(this.dataService);
    this.networkService.publish(
        new PacketArmorSettingsState(material, armorSettings.enabled(material)));
  }

  public void enable(Material material) {
    ArmorSettings armorSettings = this.settingsCache.get();
    armorSettings.enable(material);
    Recipe recipe = this.armorRecipeCache.get(material).orElseThrow(NullPointerException::new);
    Bukkit.addRecipe(recipe);
  }

  public void disable(Material material) {
    ArmorSettings armorSettings = this.settingsCache.get();

    armorSettings.disable(material);
    ItemUtil.removeRecipe(material);
  }

  @Override
  public void update(ArmorSettings entity) {
    // super.update(entity);
  }

  @PacketHandler
  public void onPacket(PacketArmorSettingsState packet) {
    if (packet.isState()) {
      enable(packet.getMaterial());
    } else disable(packet.getMaterial());
  }
}
