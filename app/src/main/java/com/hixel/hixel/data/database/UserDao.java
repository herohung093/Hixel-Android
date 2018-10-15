package com.hixel.hixel.data.database;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.hixel.hixel.data.entities.User;
import java.util.List;

/**
 * Data Access Object for the users table.
 * This uses LiveData so we can notify observers when the data changes.
 */
@Dao
public interface UserDao {

    @Insert(onConflict = REPLACE)
    void saveUser(User user);

    @Query("SELECT * FROM user")
    LiveData<User> getUser();

    @Query("SELECT * FROM user")
    User get();

    @Query("SELECT companies FROM user")
    List<String> getTickers();

    @Update
    void updateUser(User user);
}
