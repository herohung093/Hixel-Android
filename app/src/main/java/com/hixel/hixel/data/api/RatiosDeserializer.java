package com.hixel.hixel.data.api;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class RatiosDeserializer<T> implements JsonDeserializer<T> {

    private static final String TAG = RatiosDeserializer.class.getSimpleName();

    @Override
    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        // Get the "ratios" element from the parsed JSON
        JsonElement ratios = jsonElement.getAsJsonObject()
                .get("financialDataEntries")
                .getAsJsonArray();

        Log.d(TAG, "deserialize: " + ratios.toString());

        // Deserialize it.
        return new Gson().fromJson(ratios, type);
    }
}
