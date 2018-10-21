package com.hixel.hixel.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import com.hixel.hixel.data.entities.Company;
import java.util.List;

@Dao
public interface CompanyDataDao {
    @Query("SELECT * FROM company_data")
    LiveData<List<Company>> loadCompany();
}
