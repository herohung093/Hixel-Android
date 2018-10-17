package com.hixel.hixel.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.hixel.hixel.data.entities.Company;
import java.util.List;

/**
 * Data Access Object for interacting with the companies table. Return LiveData so we c
 * an notify observers when the data changes, and the UI will update.
 */
@Dao
public interface CompanyDao {
    @Query("SELECT * FROM companies")
    LiveData<List<Company>> load();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveCompanies(List<Company> companies);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveCompany(Company company);
}
