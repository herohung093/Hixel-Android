package com.hixel.hixel.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.entities.User;

/**
 * Application database store the companies and the user data
 */
// TODO: schema and migration handling
@Database(entities = {Company.class, User.class}, version = 19, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract CompanyDao companyDao();

    public abstract UserDao userDao();
}
