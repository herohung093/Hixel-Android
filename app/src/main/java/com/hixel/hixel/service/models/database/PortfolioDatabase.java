package com.hixel.hixel.service.models.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.hixel.hixel.service.models.Portfolio;

@Database(entities = { Portfolio.class }, version = 1)
public abstract class PortfolioDatabase extends RoomDatabase {

    private static final String DB_NAME = "portfolioDatabase.db";
    private static volatile PortfolioDatabase instance;

    static synchronized PortfolioDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }

        return instance;
    }

    private static PortfolioDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                PortfolioDatabase.class,
                DB_NAME).build();
    }

    public abstract PortfolioDao getAll();
}
