package com.hixel.hixel.service.models.database;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.util.ArrayList;

@Dao
public interface CompanyDao {

    @Insert(onConflict = REPLACE)
    void save(CompanyEntity company);

    @Query("SELECT * FROM CompanyEntity")
    MutableLiveData<ArrayList<CompanyEntity>> loadCompanies();

    @Query("SELECT * FROM CompanyEntity WHERE cik = :currentCik")
    CompanyEntity hasCompany(String currentCik);
}
