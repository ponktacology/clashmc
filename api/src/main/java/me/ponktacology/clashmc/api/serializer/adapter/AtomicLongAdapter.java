package me.ponktacology.clashmc.api.serializer.adapter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicLong;

public class AtomicLongAdapter implements JsonSerializer<AtomicLong>, JsonDeserializer<AtomicLong> {


    @Override
    public AtomicLong deserialize(
             JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

        long value = jsonElement.getAsJsonPrimitive().getAsLong();

        return new AtomicLong(value);
    }


    @Override
    public JsonElement serialize(
             AtomicLong value, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(value.get());
    }
}
