package com.hixel.hixel.data.database;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import com.hixel.hixel.data.entities.company.Company;
import com.hixel.hixel.data.entities.company.FinancialDataEntries;
import com.hixel.hixel.data.entities.company.Identifiers;
import java.util.List;
import timber.log.Timber;

/**
 * Entry point for handling database request due to this being the 'root' class
 * for Api. Ensures we map data correctly when reading Company objects into the db.
 */
@Dao
public abstract class IdentifiersDao {

    @Query("SELECT * FROM Identifiers")
    @Transaction
    public abstract LiveData<List<Company>> loadCompanies();

    public void insertCompanies(List<Company> companies) {
        for (Company c : companies) {
            Timber.w(c.getIdentifiers().getName());
            if (c.getDataEntries() != null) {
                insertDataEntries(c, c.getDataEntries());
            }
        }
    }

    private void insertDataEntries(Company c, List<FinancialDataEntries> dataEntries) {
        for (FinancialDataEntries entry : dataEntries) {
            entry.setCik(c.getIdentifiers().getCik());
            _insertIdentifier(c.getIdentifiers());
        }

        _insertAllDataEntries(dataEntries);
    }

    @Insert(onConflict = REPLACE)
    abstract void _insertIdentifier(Identifiers identifier);

    @Insert
    abstract void _insertAllDataEntries(List<FinancialDataEntries> dataEntries);

}
