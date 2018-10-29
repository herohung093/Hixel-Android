package com.hixel.hixel.data.entities.company;

import android.arch.persistence.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class RatioConverter {

    @TypeConverter
    public static List<Ratios> stringToSomeObjectList(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Ratios>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<Ratios> someObjects) {
        Gson gson = new Gson();
        return gson.toJson(someObjects);
    }
}
