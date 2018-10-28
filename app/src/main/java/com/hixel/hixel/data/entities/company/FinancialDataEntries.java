package com.hixel.hixel.data.entities.company;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

/**
 * Db Entity that holds the year of the entry, a list of Ratios for that year
 * (to be re-implemented), and the cik of the Company that is responsible for
 * the entries.
 */
@Entity(foreignKeys = {
                @ForeignKey(
                    entity = Identifiers.class,
                    parentColumns = "id",
                    childColumns = "identifier_id"
        )}// ,
        // indices = @Index(value = "identifier_id", name = "id")
)
public class FinancialDataEntries {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("year")
    @ColumnInfo(name="year")
    public int year;

    @ColumnInfo(name="identifier_id")
    public String identifierId;

    @Embedded
    @SerializedName("ratios")
    public Ratios ratios;

    public FinancialDataEntries() {}

    public int getYear() {
        return year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setIdentifierId(String identifierId) {
        this.identifierId = identifierId;
    }

    public String getIdentifierId() {
        return identifierId;
    }

    public Ratios getRatios() {
        return ratios;
    }

    public void setRatios(Ratios ratios) {
        this.ratios = ratios;
    }
}
