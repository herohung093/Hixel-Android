package com.hixel.hixel.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.hixel.hixel.data.entities.Company;

/**
 * Room DB that contains the companies table.
 */

// TODO: schema and migration handling
@Database(entities = {Company.class}, version = 16, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract CompanyDao companyDao();

}
