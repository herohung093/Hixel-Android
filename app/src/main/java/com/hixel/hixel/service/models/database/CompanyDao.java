package com.hixel.hixel.service.models.database;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface CompanyDao {

    @Insert(onConflict = REPLACE)
    void save(CompanyEntity company);

    @Query("SELECT * FROM CompanyEntity")
    LiveData<CompanyEntity> loadCompanies();

    @Query("SELECT * FROM CompanyEntity WHERE cik = :currentCik")
    CompanyEntity hasCompany(String currentCik);
}
