package me.ponktacology.clashmc.sector.configuration;

import me.ponktacology.loader.LoaderPlugin;
import me.ponktacology.clashmc.api.configuration.GsonConfiguration;
import me.ponktacology.clashmc.sector.api.sector.SectorType;


import java.util.Map;
import java.util.TreeMap;

public class SectorConfiguration extends GsonConfiguration {

  public SectorConfiguration() {
    super("Sector/config", LoaderPlugin.INSTANCE.getConfigurationSerializer());
  }


  @Override
  public Map<String, Object> getDefaults() {
    Map<String, Object> defaults = new TreeMap<>();

    defaults.put("min-z", -100.0);
    defaults.put("max-z", 100.0);
    defaults.put("min-x", -100.0);
    defaults.put("max-x", 100.0);
    defaults.put("type", SectorType.DEFAULT);

    return defaults;
  }

  public int getMinX() {
    return Double.valueOf((double) get("min-x")).intValue();
  }

  public int getMaxX() {
    return Double.valueOf((double) get("max-x")).intValue();
  }

  public int getMinZ() {
    return Double.valueOf((double) get("min-z")).intValue();
  }

  public int getMaxZ() {
    return Double.valueOf((double) get("max-z")).intValue();
  }

  public SectorType getSectorType() {
    return SectorType.valueOf(get("type"));
  }

}
