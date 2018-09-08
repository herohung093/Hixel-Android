package com.hixel.hixel.db.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.hixel.hixel.service.models.database.CompanyEntity;

@Database(entities = {CompanyEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // SINGLETON
    private static AppDatabase INSTANCE;

    // DAO
    public abstract CompanyDao companyDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "user-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            //.allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
