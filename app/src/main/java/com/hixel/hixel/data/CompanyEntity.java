package com.hixel.hixel.data;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import android.support.annotation.NonNull;

/**
 * Company entity acts as an entry point to other models
 */

@Entity(tableName = "companies")
public class CompanyEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @Embedded
    private Identifiers identifiers;

    public CompanyEntity() { }

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    public Identifiers getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(Identifiers identifiers) {
        this.identifiers = identifiers;
    }

    // NOTE: TEMPORARY METHOD FOR DISPLAY.
    public double getRatio() {
        return 10.0;
    }
}
