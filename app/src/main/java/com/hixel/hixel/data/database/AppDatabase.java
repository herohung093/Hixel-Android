package com.hixel.hixel.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.entities.User;

/**
 * Application database stores the companies and the user data.
 */
// TODO: schema and migration handling
@Database(entities = {Company.class, User.class}, version = 20, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract CompanyDao companyDao();

    public abstract UserDao userDao();
}
