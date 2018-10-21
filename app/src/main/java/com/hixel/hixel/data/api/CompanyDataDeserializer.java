package com.hixel.hixel.data.api;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import timber.log.Timber;

public class CompanyDataDeserializer<T> implements JsonDeserializer<T> {
    @Override
    public T deserialize(JsonElement jsonElement, Type type,
            JsonDeserializationContext jdc) throws JsonParseException {
        String des = jsonElement.getAsJsonObject().get("financialDataEntries").toString();

        Timber.d(des);
        return new Gson().fromJson(des, type);
    }
}
