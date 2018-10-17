package com.hixel.hixel.data.entities;

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
    private static Gson gson = new Gson();

    /**
     * Converts a String into a List of strings for retrieving the data from room.
     *
     * @param data the string of data stored in Room
     * @return a list of tickers
     */
    @TypeConverter
    public static List<String> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<String>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    /**
     * Converts a List of Strings into a single string to store the data in Room.
     *
     * @param someObjects the tickers
     * @return a single String of those tickers.
     */
    @TypeConverter
    public static String someObjectListToString(List<String> someObjects) {
        return gson.toJson(someObjects);
    }
}
