package com.hixel.hixel.data.database;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import com.hixel.hixel.data.entities.company.Company;
import com.hixel.hixel.data.entities.company.Identifiers;
import java.util.List;

/**
 * Entry point for handling database request due to this being the 'root' class
 * for Api. Ensures we map data correctly when reading Company objects into the db.
 */
@Dao
public interface IdentifiersDao {

    //@Query("SELECT * FROM Identifiers " +
    //"INNER JOIN FinancialDataEntries ON FinancialDataEntries.identifier_id = Identifiers.id")
    @Query("SELECT * FROM Identifiers")
    @Transaction
    LiveData<List<Company>> loadAllCompanies();

    @Query("SELECT * FROM Identifiers")
    LiveData<List<Identifiers>> getAllIdentifiers();

    @Insert(onConflict = REPLACE)
    void insertIdentifier(Identifiers identifiers);

    @Query("DELETE FROM Identifiers")
    void deleteAll();
}
