package com.hixel.hixel.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.hixel.hixel.data.models.Company;

/**
 * Room DB that contains the companies table.
 */

// TODO: Rename class from AppDB to CompanyDB ??
// TODO: schema and migration handling
@Database(entities = {Company.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract CompanyDao companyDao();

}
