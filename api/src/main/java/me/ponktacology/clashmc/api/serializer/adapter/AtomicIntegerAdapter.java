package me.ponktacology.clashmc.api.serializer.adapter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerAdapter implements JsonSerializer<AtomicInteger>, JsonDeserializer<AtomicInteger> {

    
    @Override
    public AtomicInteger deserialize(
             JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

        int value = jsonElement.getAsJsonPrimitive().getAsInt();

        return new AtomicInteger(value);
    }

    
    @Override
    public JsonElement serialize(
             AtomicInteger value, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(value.get());
    }
}
