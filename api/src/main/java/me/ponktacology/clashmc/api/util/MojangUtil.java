package me.ponktacology.clashmc.api.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.experimental.UtilityClass;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class MojangUtil {

  private static final UUID INVALID_UUID = UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5");
  private static final Cache<String, UUID> UUID_CACHE =
      Caffeine.newBuilder().expireAfterAccess(12, TimeUnit.HOURS).build();
  private static final Cache<UUID, Pair<String, String>> CACHED_SKINS =
      Caffeine.newBuilder().expireAfterAccess(12, TimeUnit.HOURS).build();

  public static Optional<UUID> getUuid( String name, boolean premiumOnly) {
    String sURL = "https://api.ashcon.app/mojang/v2/user/" + name.replace(" ", "");

    if (premiumOnly) {
      UUID uuid = UUID_CACHE.getIfPresent(name);

      if (uuid != null) {
        return Optional.ofNullable(uuid.equals(INVALID_UUID) ? null : uuid);
      }
    }

    try {
      URL url = new URL(sURL);
      URLConnection request = url.openConnection();
      request.connect();

      JsonObject jsonObject = null;
      try {
        jsonObject =
            new JsonParser()
                .parse(new InputStreamReader((InputStream) request.getContent()))
                .getAsJsonObject();
      } catch (IOException e) {
        if (!(e instanceof FileNotFoundException)) {
          e.printStackTrace();
          return Optional.empty();
        }
      }

      if (jsonObject != null) {
        String id = jsonObject.get("uuid").getAsString();
        UUID uuid = UUID.fromString(id);
        UUID_CACHE.put(name, uuid);
        return Optional.of(uuid);
      }
    } catch (Exception e) {
      if (!(e instanceof FileNotFoundException)) {
        e.printStackTrace();
      }
    }

    UUID_CACHE.put(name, INVALID_UUID);

    return Optional.ofNullable(
        premiumOnly ? null : UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes()));
  }

  
  private static UUID toUUID( String mojangUUID) {
    StringBuilder uuid = new StringBuilder();
    char[] arr = mojangUUID.toCharArray();
    try {
      for (int i = 0; i < 31; i++) {
        uuid.append(arr[i]);
        if (i == 7 || i == 11 || i == 15 || i == 19) {
          uuid.append("-");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

    return UUID.fromString(uuid.toString());
  }

  
  public static Pair<String, String> getSkinAndSignature( UUID uuid) {
    try {
      Pair<String, String> cached = CACHED_SKINS.getIfPresent(uuid);

      if (cached != null) {
        return cached;
      }

      URL url = new URL("https://api.ashcon.app/mojang/v2/user/" + uuid.toString());
      URLConnection request = url.openConnection();
      request.connect();

      JsonObject jsonObject;
      try {
        jsonObject =
            new JsonParser()
                .parse(new InputStreamReader((InputStream) request.getContent()))
                .getAsJsonObject();
      } catch (IOException e) {
        return null;
      }

      JsonObject jsonElement = jsonObject.getAsJsonObject("textures");
      JsonObject jsonElement2 = jsonElement.get("raw").getAsJsonObject();

      Pair<String, String> pair =
          new Pair<>(
              jsonElement2.get("value").getAsString(), jsonElement2.get("signature").getAsString());
      CACHED_SKINS.put(uuid, pair);
      return pair;
    } catch (IOException e) {
      if (!(e instanceof FileNotFoundException)) {
        e.printStackTrace();
      }

      return null;
    }
  }
}
