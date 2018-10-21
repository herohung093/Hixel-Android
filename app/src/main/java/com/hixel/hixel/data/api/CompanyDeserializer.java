package com.hixel.hixel.data.api;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.lang.reflect.Type;
import timber.log.Timber;

/**
 *  Unwraps the JSON received from the server to be used by the Company Entity,
 *  removing unnecessary info so less data is stored in the RoomDB.
 */
public class CompanyDeserializer<T> implements JsonDeserializer<T> {

    @Override
    public T deserialize(JsonElement jsonElement, Type type,
            JsonDeserializationContext jdc) throws JsonParseException {
        String des = jsonElement.getAsJsonObject().get("identifiers").toString();
        Timber.d(des);
        return new Gson().fromJson(des, type);
    }
}
