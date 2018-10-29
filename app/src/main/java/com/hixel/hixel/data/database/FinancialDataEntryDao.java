package com.hixel.hixel.data.database;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.hixel.hixel.data.entities.company.FinancialDataEntries;
import java.util.List;

/**
 * NOTE: Currently here to satisfy DI, all db requests are handled though the
 * IdentifiersDao.java.
 */
@Dao
public interface FinancialDataEntryDao {

    @Query("SELECT * FROM FinancialDataEntries")
    LiveData<List<FinancialDataEntries>> getAllFinancialDataEntries();

    @Insert(onConflict = REPLACE)
    void insertFinancialDataEntry(FinancialDataEntries financialDataEntries);

    @Query("DELETE FROM FinancialDataEntries")
    void deleteAll();
}
