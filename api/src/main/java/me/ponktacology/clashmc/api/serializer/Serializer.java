package me.ponktacology.clashmc.api.serializer;

import me.ponktacology.clashmc.api.serializer.exception.SerializationException;

public interface Serializer {

  <T> T deserialize(String json, Class<T> entity) throws SerializationException;

  <T> String serialize(T entity) throws SerializationException;
}
