package me.ponktacology.clashmc.api.serializer.adapter;

import com.google.gson.*;


import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicBooleanAdapter implements JsonSerializer<AtomicBoolean>, JsonDeserializer<AtomicBoolean> {

    
    @Override
    public AtomicBoolean deserialize(
             JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

        boolean value = jsonElement.getAsJsonPrimitive().getAsBoolean();

        return new AtomicBoolean(value);
    }

    
    @Override
    public JsonElement serialize(
             AtomicBoolean value, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(value.get());
    }
}
