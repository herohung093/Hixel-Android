package com.hixel.hixel.data.database;

import android.arch.persistence.room.TypeConverter;
import java.util.Date;

/**
 * Converts between Date object and Long, used for using Dates
 * in Application DB.
 */
public class DateConverter {
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
