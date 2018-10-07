package com.hixel.hixel.data.api;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.lang.reflect.Type;

/**
 *  Unwraps the JSON received from the server to be used by the Company POJO.
 */
public class CompanyDeserializer<T> implements JsonDeserializer<T> {

    private static final String TAG = CompanyDeserializer.class.getSimpleName();

    @Override
    public T deserialize(JsonElement jsonElement, Type type,
            JsonDeserializationContext jdc) throws JsonParseException {

        String identifiers = jsonElement.getAsJsonObject().get("identifiers").toString();
        String financialDataEntries = jsonElement.getAsJsonObject().get("financialDataEntries").toString();

        String formattedResp1 = identifiers.substring(0, identifiers.length() - 1);
        String formattedResp2 = "," + financialDataEntries.substring(2, financialDataEntries.length() - 2) + "}";
        // String combined = formattedResp1 + formattedResp2;

        String formattedResp3 = "{" + formattedResp2.substring(1, formattedResp2.length() - 2) + "}}";


        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(formattedResp3);

        String ratios = je.getAsJsonObject().get("ratios").toString();
        String formattedResp5 = "," + ratios.substring(1, ratios.length());
        String combined = formattedResp1 + formattedResp5;

        // String prettyJsonString = gson.toJson(je);

        Log.d(TAG, "deserialize: " + combined);

        return new Gson().fromJson(combined, type);
    }
}
