package com.hixel.hixel.data.entities;

import android.arch.persistence.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class CompanyDataTypeConverter {
    @TypeConverter
    public static List<CompanyData> stringToCompanyDataList(String data) {
        Gson gson = new Gson();

        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Company>>() {}.getType();

        return gson.fromJson(data, listType);
    }
    @TypeConverter
    public static String companyDataToString(List<CompanyData> data) {
        Gson gson = new Gson();

        return gson.toJson(data);
    }
}
