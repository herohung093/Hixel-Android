package com.hixel.hixel.data.entities.user;

import android.arch.persistence.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Converts between String and List of Strings for company tickers, this is used to convert the
 * Type of data when entering and leaving the RoomDB.
 */
public class PortfolioTypeConverter {
    @TypeConverter
    public static List<Ticker> stringToTickerList(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Ticker>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String tickerListToString(List<Ticker> someObjects) {
        Gson gson = new Gson();
        return gson.toJson(someObjects);
    }
}
