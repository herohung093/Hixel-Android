package com.hixel.hixel.data.source.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.hixel.hixel.data.CompanyEntity;
import java.util.List;

/**
 * Data Access Object for the companies table.
 * This uses LiveData so we can notify observers when the data changes.
 */

// TODO: Needs more methods for all activities.
@Dao
public interface CompanyDao {

    @Query("SELECT * FROM companies")
    LiveData<List<CompanyEntity>> load();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveCompanies(List<CompanyEntity> companies);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveCompany(CompanyEntity company);

}
