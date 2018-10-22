package com.hixel.hixel.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import com.hixel.hixel.data.entities.FinancialDataEntries;
import java.util.List;

@Dao
public interface FinancialDataEntryDao {

    @Query("SELECT * FROM FinancialDataEntries")
    List<FinancialDataEntries> getAllYears();
}
