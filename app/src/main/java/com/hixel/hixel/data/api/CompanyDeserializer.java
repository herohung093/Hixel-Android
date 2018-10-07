package com.hixel.hixel.data.api;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class CompanyDeserializer<T> implements JsonDeserializer<T> {

    @Override
    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        // Get the "identifiers" element from the parsed JSON
        JsonElement identifiers = jsonElement.getAsJsonObject().get("identifiers");

        // Deserialize it.
        return new Gson().fromJson(identifiers, type);
    }
}
