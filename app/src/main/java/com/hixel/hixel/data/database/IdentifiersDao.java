package com.hixel.hixel.data.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import com.hixel.hixel.data.entities.company.Company;
import com.hixel.hixel.data.entities.company.FinancialDataEntries;
import com.hixel.hixel.data.entities.company.Identifiers;
import java.util.List;

/**
 * Entry point for handling database request due to this being the 'root' class
 * for Api. Ensures we map data correctly when reading Company objects into the db.
 */
@Dao
public abstract class IdentifiersDao {

    @Query("SELECT * FROM Identifiers")
    @Transaction
    public abstract LiveData<List<Company>> loadCompanies();

    @Query("SELECT * FROM Identifiers WHERE ticker = :ticker")
    @Transaction
    public abstract LiveData<Company> loadCompany(String ticker);

    public void insertCompanies(List<Company> companies) {
        for (Company c : companies) {
            insertIdentifiers(c.getIdentifiers());
            insertFinancialData(c.getDataEntries());
        }
    }

    @Insert
    abstract void insertIdentifiers(Identifiers identifier);

    @Insert
    abstract void insertFinancialData(List<FinancialDataEntries> dataEntry);

}
