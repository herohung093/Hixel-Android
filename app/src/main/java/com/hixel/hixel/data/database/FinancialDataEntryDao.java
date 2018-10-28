package com.hixel.hixel.data.database;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import com.hixel.hixel.data.entities.company.Company;
import com.hixel.hixel.data.entities.company.FinancialDataEntries;
import com.jakewharton.rxbinding2.widget.SearchViewQueryTextEvent;
import java.util.List;

/**
 * NOTE: Currently here to satisfy DI, all db requests are handled though the
 * IdentifiersDao.java.
 */
@Dao
public interface FinancialDataEntryDao {

    @Query("SELECT Identifiers.id, Identifiers.name, Identifiers.ticker, FinancialDataEntries.year " +
            "FROM FinancialDataEntries " +
           "INNER JOIN Identifiers ON FinancialDataEntries.identifier_id = Identifiers.id")
    @Transaction
    LiveData<List<Company>> getAllCompanies();

    @Query("SELECT * FROM FinancialDataEntries")
    LiveData<List<FinancialDataEntries>> getAllFinancialDataEntries();

    @Insert(onConflict = REPLACE)
    void insertFinancialDataEntry(FinancialDataEntries financialDataEntries);

    @Query("DELETE FROM FinancialDataEntries")
    void deleteAll();
}
