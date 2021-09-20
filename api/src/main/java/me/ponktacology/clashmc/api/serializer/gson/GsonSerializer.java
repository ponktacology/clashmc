package me.ponktacology.clashmc.api.serializer.gson;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.serializer.Serializer;
import me.ponktacology.clashmc.api.serializer.exception.SerializationException;

@RequiredArgsConstructor
@Getter
public class GsonSerializer implements Serializer {

  
  private final Gson gson;

  @Override
  public <T> T deserialize(String json,  Class<T> entity) throws SerializationException {
    try {
      return gson.fromJson(json, entity);
    } catch (Exception e) {
      throw new SerializationException(
          "Couldn't deserialize entity " + entity.getSimpleName() + " json: " + json, e);
    }
  }

  @Override
  public <T> String serialize( T entity) throws SerializationException {
    try {
      return gson.toJson(entity);
    } catch (Exception e) {
      throw new SerializationException(
          "Couldn't serialize entity " + entity.getClass().getSimpleName(), e);
    }
  }
}
