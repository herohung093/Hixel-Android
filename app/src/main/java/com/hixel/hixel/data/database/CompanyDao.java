package com.hixel.hixel.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.entities.CompanyData;
import java.util.List;

/**
 * Data Access Object for interacting with the companies table. Return LiveData so we can
 * notify observers when the data changes, and the UI will update.
 */
@Dao
public interface CompanyDao {

    /**
     * Retrieve a LiveData List of all Company objects
     *
     * @return LiveData List of all Company objects
     */
    @Query("SELECT * FROM companies")
    LiveData<List<Company>> loadCompanies();

    @Query("SELECT * FROM companies WHERE ticker = :ticker")
    LiveData<Company> loadCompany(String ticker);

    @Query("SELECT * FROM company_data WHERE cik = :cik")
    LiveData<List<CompanyData>> loadCompanyData(String cik);

    /**
     * Inserts a List of companies into the database, using a replacement strategy for any
     * two companies that have the same primary key.
     *
     * @param companies List of companies to save
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCompanies(List<Company> companies);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCompanyData(List<CompanyData> companyData);

    /**
     * Inserts a single company into the database, using a replacement strategy for any two
     * companies that have the same primary key.
     *
     * @param company The company to insert into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCompany(Company company);
}
