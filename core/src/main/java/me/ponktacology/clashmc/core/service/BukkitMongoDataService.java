package me.ponktacology.clashmc.core.service;

import me.ponktacology.clashmc.api.serializer.Serializer;
import me.ponktacology.clashmc.api.service.data.mongo.MongoDataService;
import me.ponktacology.clashmc.core.service.exception.SyncDataManageException;
import org.bukkit.Bukkit;

import java.util.Optional;

public class BukkitMongoDataService extends MongoDataService {
  public BukkitMongoDataService(String connectionUrl, Serializer serializer) {
    super(connectionUrl, serializer);
  }

  
  @Override
  public <V, T> Optional<V> load(T key, Class<V> type) {
    if (Bukkit.isPrimaryThread()) {
      new SyncDataManageException("Loading data from mongodb on main thread.").printStackTrace();
    }

    return super.load(key, type);
  }

  
  @Override
  public <V> void save(V entity) {
    if (Bukkit.isPrimaryThread()) {
     new SyncDataManageException("Saving data to mongodb on main thread").printStackTrace();
    }

    super.save(entity);
  }

  
  @Override
  public <V> void delete(V entity) {
    if (Bukkit.isPrimaryThread()) {
      new SyncDataManageException("Deleting data from mongodb on main thread").printStackTrace();
    }

    super.delete(entity);
  }
}
