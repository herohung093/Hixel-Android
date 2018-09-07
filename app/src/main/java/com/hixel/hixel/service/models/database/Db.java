package com.hixel.hixel.service.models.database;

import android.arch.persistence.room.RoomDatabase;

public abstract class Db extends RoomDatabase {

    // SINGLETON
    private static volatile Db INSTANCE;

    // DAO
    public abstract CompanyDao companyDao();
}
