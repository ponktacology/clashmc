package me.ponktacology.clashmc.api.service.data;

import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;
import me.ponktacology.clashmc.api.service.Service;
import org.bson.conversions.Bson;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public interface DataService extends Service {

  Map<Class<?>, String> collections = new ConcurrentHashMap<>();
  Map<Class<?>, String> databases = new ConcurrentHashMap<>();
  Map<Class<?>, String> keys = new ConcurrentHashMap<>();

  /*
   Loads all data where document key is @key and type is @type
  */
  <V, T> Optional<V> load(T key, Class<V> type);

  /*
   Loads data where document with field @field contains value @key and is type @type
  */

  default <V, T> Set<V> loadIfFieldContains(String field, T key, Class<V> type) {
    return Collections.emptySet();
  }

  /*
   Loads all data where document is type @type
  */

  default <V> List<V> loadAll(Class<V> type) {
    return Collections.emptyList();
  }

  <V> void save(V entity);

  <V> void delete(V entity);

  default void shutdown() {
    // empty
  }

  default Optional<Object> getKey( Object entity) {
    Class<?> clazz = entity.getClass();
    try {
      String fieldName =
          keys.computeIfAbsent(
              clazz,
              c -> {
                for (Field field : c.getDeclaredFields()) {
                  if (!field.isAnnotationPresent(SerializedName.class)) continue;

                  SerializedName serializedName = field.getAnnotation(SerializedName.class);

                  if (!"_id".equals(serializedName.value())) continue;

                  return field.getName();
                }

                return null;
              });

      if (!Strings.isNullOrEmpty(fieldName)) {
        return Optional.ofNullable(clazz.getDeclaredField(fieldName).get(entity));
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return Optional.empty();
  }

  default <V> long count(Class<V> type, Bson filter) {
    return -1;
  }


  default <V> List<V> sorted(Class<V> type, Bson filter, int limit) {
    return Collections.emptyList();
  }
}
