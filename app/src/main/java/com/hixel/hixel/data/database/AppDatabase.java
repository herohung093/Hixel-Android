package com.hixel.hixel.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.entities.User;

/**
 * Application database stores Company and User data
 */
// TODO: schema and migration handling
@Database(entities = {Company.class, User.class}, version = 20, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    /**
     * DB Instance to be used to interact with Room throughout the application.
     *
     * NOTE: Do not remove. The INSTANCE is used by Dagger at compile time.
     */
    private static AppDatabase INSTANCE;

    /**
     * Create a CompanyDao Instance
     * @return CompanyDao Instance
     */
    public abstract CompanyDao companyDao();

    /**
     * Creates a UserDao Instance.
     * @return UserDao Instace.
     */
    public abstract UserDao userDao();
}
