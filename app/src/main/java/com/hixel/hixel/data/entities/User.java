package com.hixel.hixel.data.entities;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

/**
 * Immutable User object.
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

    @Embedded
    private Portfolio portfolio;

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

    public Portfolio getPortfolio() { return portfolio; }

    public void setPortfolio(Portfolio portfolio) { this.portfolio = portfolio; }
}
