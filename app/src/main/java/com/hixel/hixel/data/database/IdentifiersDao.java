package com.hixel.hixel.data.database;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.entities.FinancialDataEntries;
import com.hixel.hixel.data.entities.Identifiers;
import java.util.ArrayList;
import java.util.List;

/**
 * Entry point for handling database request due to this being the 'root' class
 * for Api. Ensures we map data correctly when reading Company objects into the db.
 */
@Dao
public abstract class IdentifiersDao {

    public void insertCompanies(List<Company> companies) {
        List<Identifiers> identifiers = new ArrayList<>();

        for (Company company : companies) {
            Identifiers identifier = company.getIdentifiers();
            identifiers.add(identifier);

            insertDataEntries(company.getDataEntries(), identifier.getCik());
        }

        _insertIdentifiers(identifiers);
    }

    private void insertDataEntries(List<FinancialDataEntries> dataEntries, String cik) {
        for (FinancialDataEntries entry : dataEntries) {
            entry.setIdentifiersCik(cik);
        }

        _insertDataEntries(dataEntries);
    }

    @Query("SELECT * FROM Identifiers")
    public abstract LiveData<List<Company>> loadCompanies();

    @Insert(onConflict = REPLACE)
    abstract void insertIdentifiers(List<Identifiers> identifier);

    @Insert
    abstract void _insertIdentifiers(List<Identifiers> identifiers);

    @Insert
    abstract void _insertDataEntries(List<FinancialDataEntries> dataEntries);
}
