package com.hixel.hixel.data.entities.user;

import android.arch.persistence.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hixel.hixel.data.entities.company.Ratios;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Converts between String and List of Strings for company tickers, this is used to convert the
 * Type of data when entering and leaving the RoomDB.
 */
public class PortfolioTypeConverter {
    @TypeConverter
    public static List<Ticker> stringToSomeObjectList(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Ticker>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<Ticker> someObjects) {
        Gson gson = new Gson();
        return gson.toJson(someObjects);
    }
}
