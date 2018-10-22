package com.hixel.hixel.data.database;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.hixel.hixel.data.entities.user.User;
import java.util.List;

/**
 * Data Access Object for interacting with the companies table. Return LiveData so we
 * can notify observers when the data changes, and the UI will update.
 */
@Dao
public interface UserDao {

    /**
     * Inserts a user into the database, replacing any user that has the same primary key.
     *
     * @param user the user to be inserted
     */
    @Insert(onConflict = REPLACE)
    void saveUser(User user);

    /**
     * Retrieves all users from the application database, however, there should only ever be 1
     * active user in the database.
     *
     * @return a LiveData user
     */
    @Query("SELECT * FROM user")
    LiveData<User> getUser();

    /**
     * Retrieves a user from the application database, however, there should only ever be 1
     * active user in the database.
     *
     * @return a User object
     */
    @Query("SELECT * FROM user")
    User get();

    /**
     * Retrieves all the tickers a user has in their portfolio.
     *
     * @return A list of tickers
     */
    @Query("SELECT companies FROM user")
    List<String> getTickers();

    /**
     * Updates the currently active user.
     *
     * @param user the updated user object.
     */
    @Update
    void updateUser(User user);
}
