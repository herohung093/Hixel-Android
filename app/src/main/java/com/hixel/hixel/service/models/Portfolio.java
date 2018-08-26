package com.hixel.hixel.service.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Portfolio {

    @PrimaryKey
    public final int id; // TODO: Use the CIK for a company as the ID.
    public final String name;

    public Portfolio(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
