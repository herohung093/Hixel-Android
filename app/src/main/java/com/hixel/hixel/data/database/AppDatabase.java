package com.hixel.hixel.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import com.hixel.hixel.data.entities.company.FinancialDataEntries;
import com.hixel.hixel.data.entities.company.Identifiers;
import com.hixel.hixel.data.entities.company.RatioConverter;
import com.hixel.hixel.data.entities.user.User;

/**
 * Application database stores Company and User data.
 */
@Database(entities = {Identifiers.class, FinancialDataEntries.class, User.class},
        version = 36, exportSchema = false)
@TypeConverters(RatioConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    /**
     * DB Instance to be used to interact with Room throughout the application.
     *
     * NOTE: Do not remove. The INSTANCE is used by Dagger at compile time.
     */
    // TODO: Check if this is needed
    private static AppDatabase INSTANCE;

    public abstract IdentifiersDao identifiersDao();

    public abstract FinancialDataEntryDao financialDataEntryDao();

    /**
     * Creates a UserDao Instance.
     *
     * @return UserDao Instance.
     */
    public abstract UserDao userDao();
}
