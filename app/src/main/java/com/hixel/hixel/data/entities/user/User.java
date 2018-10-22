package com.hixel.hixel.data.entities.user;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

/**
 * User Entity, stores pertinent data from the server in the application
 */
// TODO: Need a string of tickers from the user.
@Entity(tableName = "user")
public class User {

    @PrimaryKey
    @NonNull
    @SerializedName("email")
    private String email;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    /**
     * Allows storing the List of tickers in the db.
     */
    @Embedded
    private Portfolio portfolio;


    /**
     * Minimal User constructor to lower db overhead.
     *
     * @param email email of the user - used as a PK
     * @param firstName First Name of the user
     * @param lastName Last Name of the user
     */
    public User(@NonNull String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }
}
