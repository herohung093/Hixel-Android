package com.hixel.hixel.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.hixel.hixel.data.entities.Company;
import java.util.List;

/**
 * Data Access Object for the companies table.
 * This uses LiveData so we can notify observers when the data changes.
 */

// TODO: Needs more methods for all activities.
@Dao
public interface CompanyDao {

    @Query("SELECT * FROM companies")
    LiveData<List<Company>> load();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveCompanies(List<Company> companies);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveCompany(Company company);

    @Query("DELETE FROM companies")
    void deleteAll();
}
