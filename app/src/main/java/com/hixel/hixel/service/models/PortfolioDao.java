package com.hixel.hixel.service.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;

@Dao
public interface PortfolioDao {

    @Query("SELECT * FROM portfolio")
    List<Portfolio> getAll();

    @Insert
    void insert(Portfolio... portfolio);

    @Update
    void update(Portfolio... portfolio);

    @Delete
    void delete(Portfolio... portfolio);

}
