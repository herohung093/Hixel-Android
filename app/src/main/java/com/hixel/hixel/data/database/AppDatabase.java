package com.hixel.hixel.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.entities.CompanyData;
import com.hixel.hixel.data.entities.CompanyDataTypeConverter;
import com.hixel.hixel.data.entities.User;

/**
 * Application database stores Company and User data.
 */
@Database(entities = {Company.class, CompanyData.class, User.class}, version = 21, exportSchema = false)
@TypeConverters(CompanyDataTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    /**
     * DB Instance to be used to interact with Room throughout the application.
     *
     * NOTE: Do not remove. The INSTANCE is used by Dagger at compile time.
     */
    // TODO: Check if this is needed
    private static AppDatabase INSTANCE;

    /**
     * Create a CompanyDao Instance
     *
     * @return CompanyDao Instance
     */
    public abstract CompanyDao companyDao();

    public abstract CompanyDataDao companyDataDao();

    /**
     * Creates a UserDao Instance.
     *
     * @return UserDao Instance.
     */
    public abstract UserDao userDao();
}
