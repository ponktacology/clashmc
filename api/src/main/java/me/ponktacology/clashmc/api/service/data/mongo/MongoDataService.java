package me.ponktacology.clashmc.api.service.data.mongo;

import com.google.common.collect.Sets;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ReplaceOptions;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.player.PlayerWrapper;
import me.ponktacology.clashmc.api.serializer.Serializer;
import me.ponktacology.clashmc.api.serializer.exception.SerializationException;
import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.service.data.Entity;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class MongoDataService implements DataService {

  private static final Map<Class<?>, String> collections = new ConcurrentHashMap<>();
  private static final Map<Class<?>, String> databases = new ConcurrentHashMap<>();

  private final MongoClient mongoClient;
  private final Serializer serializer;

  public MongoDataService(String connectionUrl, Serializer serializer) {
    this.mongoClient = MongoClients.create(connectionUrl);
    this.serializer = serializer;
  }

  @Override
  public <V, T> Optional<V> load(T key, Class<V> type)  {
    MongoCollection<Document> collection = getCollection(type);

    log.debug("Collection: " + collection.getNamespace().getCollectionName() + ", Key: " + key);
    Document document = collection.find(Filters.eq("_id", key)).first();

    if (document == null) {
      return Optional.empty();
    }

    String json = document.toJson();
    V entity = null;
    try {
      entity = serializer.deserialize(json, type);
    } catch (SerializationException e) {
      e.printStackTrace();
    }

    return Optional.ofNullable(entity);
  }

  public <V> Optional<V> load(Bson filter, Class<V> type) {
    MongoCollection<Document> collection = getCollection(type);

    log.debug("Collection: " + collection.getNamespace().getCollectionName());
    Document document = collection.find(filter).first();

    if (document == null) {
      return Optional.empty();
    }

    String json = document.toJson();
    V entity = null;
    try {
      entity = serializer.deserialize(json, type);
    } catch (SerializationException e) {
      e.printStackTrace();
    }

    return Optional.ofNullable(entity);
  }

  @Override
  public <V, T> Set<V> loadIfFieldContains(String field, T key, Class<V> type) {
    MongoCollection<Document> collection = getCollection(type);
    Set<V> entities = Sets.newHashSet();

    for (Document document : collection.find(Filters.in(field, key))) {
      String json = document.toJson();
      V entity = null;
      try {
        entity = serializer.deserialize(json, type);
      } catch (SerializationException e) {
        e.printStackTrace();
        continue;
      }
      entities.add(entity);
    }

    return entities;
  }

  @Override
  public <V> List<V> loadAll(Class<V> type) {
    MongoCollection<Document> collection = getCollection(type);
    List<V> toReturn = new ArrayList<>();

    collection
        .find()
        .forEach(
            document -> {
              String json = document.toJson();
              V entity = null;
              try {
                entity = serializer.deserialize(json, type);
              } catch (SerializationException e) {
                e.printStackTrace();
              }

              if (entity != null) {
                toReturn.add(entity);
              }
            });

    return toReturn;
  }

  @Override
  public <V> void save(V entity) {

    Document document = null;

    try {
      String json = serializer.serialize(entity);
      document = Document.parse(json);
    } catch (SerializationException e) {
      e.printStackTrace();
    }

    if (document == null) return;

    MongoCollection<Document> collection = getCollection(entity.getClass());

    if (entity instanceof PlayerWrapper) {
      collection.createIndex(Indexes.ascending("nameLowerCase"));
    }

    Object key = document.get("_id");
    collection.replaceOne(Filters.eq("_id", key), document, new ReplaceOptions().upsert(true));
    log.debug(
        "Successfully replaced entity, key= "
            + key.toString()
            + " document= "
            + document.toString());
  }

  @Override
  public <V> void delete(V entity) {
    Document document = null;

    try {
      String json = serializer.serialize(entity);
      document = Document.parse(json);
    } catch (SerializationException e) {
      e.printStackTrace();
    }

    if (document == null) return;

    MongoCollection<Document> collection = getCollection(entity.getClass());

    Object key = document.get("_id");

    collection.findOneAndDelete(Filters.eq("_id", key));
    log.debug("Successfully deleted entity, key= " + key.toString() + " document= " + document);
  }

  @Override
  public void shutdown() {
    mongoClient.close();
  }

  @Override
  public <V> long count(Class<V> type, Bson filter) {
    return getCollection(type).countDocuments(filter);
  }

  @Override
  @SneakyThrows
  public <V> List<V> sorted(Class<V> type, Bson filter, int limit) {
    MongoCollection<Document> collection = this.getCollection(type);
    collection.createIndex(Indexes.descending("statistics.rank"));

    return collection.find().sort(filter).limit(limit).into(new ArrayList<>()).parallelStream()
        .map(
            it -> {
              try {
                return serializer.deserialize(it.toJson(), type);
              } catch (SerializationException e) {
                e.printStackTrace();
              }

              return null;
            })
        .collect(Collectors.toList());
  }

  public MongoCollection<Document> getCollection(Class<?> clazz) {
    String collection =
        collections.computeIfAbsent(
            clazz,
            c ->
                clazz.isAnnotationPresent(Entity.class)
                    ? clazz.getAnnotation(Entity.class).collection()
                    : clazz.getSimpleName());
    String database =
        databases.computeIfAbsent(
            clazz,
            c ->
                clazz.isAnnotationPresent(Entity.class)
                    ? clazz.getAnnotation(Entity.class).database()
                    : clazz.getSimpleName());

    return mongoClient.getDatabase(database).getCollection(collection);
  }
}
