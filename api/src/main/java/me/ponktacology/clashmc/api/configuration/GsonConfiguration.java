package me.ponktacology.clashmc.api.configuration;

import lombok.SneakyThrows;
import me.ponktacology.clashmc.api.serializer.Serializer;


import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public abstract class GsonConfiguration implements Configuration {

  private final String dirPath = System.getProperty("user.dir") + File.separator + "plugins" + File.separator;
  private final String fileName;
  private final Serializer serializer;

  private Map<String, Object> configMap = getDefaults();

  public GsonConfiguration(String fileName, Serializer serializer) {
    this.fileName = fileName;
    this.serializer = serializer;

    load();
  }


  @Override
  public <V> V get(String path) {
    V value = (V) configMap.get(path);

    return value == null ? (V) getDefaults().get(path) : value;
  }

  @Override
  public <V> void set(String path, V entity) {
    configMap.put(path, entity);
  }

  @SneakyThrows
  @Override
  public void load() {
    File file = new File(dirPath + fileName + ".json");

    if (!file.exists()) {
      file.getParentFile().mkdirs();
      file.createNewFile();

      String json = serializer.serialize(this.configMap);

      Files.write(
          file.toPath(),
          Collections.singleton(json),
          StandardCharsets.UTF_8,
          StandardOpenOption.WRITE);
    }

    String json = String.join("", Files.readAllLines(file.toPath()));

    this.configMap = serializer.deserialize(json, TreeMap.class);
  }

  public abstract Map<String, Object> getDefaults();
}
