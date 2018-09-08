package com.hixel.hixel.db.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.hixel.hixel.service.models.database.CompanyEntity;
import java.util.List;

@Dao
public interface CompanyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveCompanies(List<CompanyEntity> companies);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveCompany(CompanyEntity company);

    @Query("SELECT * FROM CompanyEntity")
    LiveData<List<CompanyEntity>> load();

}
