package com.hixel.hixel.data.database;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.hixel.hixel.data.entities.User;
import java.util.Date;

@Dao
public interface UserDao {

    @Insert(onConflict = REPLACE)
    void saveUser(User user);

    @Query("SELECT * FROM user WHERE email = :userEmail AND password = :userPassword")
    LiveData<User> getUser(String userEmail, String userPassword);

    @Query("SELECT * FROM user WHERE email = :userEmail AND lastRefresh > :lastRefreshMax")
    int hasUser(String userEmail, Date lastRefreshMax);

    @Query("SELECT * FROM user WHERE lastRefresh > :lastRefreshMax")
    int isStaleUser(Date lastRefreshMax);
}
