package me.ponktacology.clashmc.core.util;

import com.google.gson.*;
import org.bukkit.enchantments.Enchantment;


import java.lang.reflect.Type;

public class EnchantmentAdapter
    implements JsonDeserializer<Enchantment>, JsonSerializer<Enchantment> {

  
  @Override
  public JsonElement serialize( Enchantment src, Type typeOfSrc, JsonSerializationContext context) {
    return toJson(src);
  }

  
  @Override
  public Enchantment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    return fromJson(json);
  }

  
  public static JsonPrimitive toJson( Enchantment enchantment) {
    return new JsonPrimitive(enchantment.getName());
  }

  
  public static Enchantment fromJson( JsonElement jsonElement) {
    if (jsonElement == null) {
      return null;
    }

    JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();

    return Enchantment.getByName(jsonPrimitive.getAsString());
  }
}
