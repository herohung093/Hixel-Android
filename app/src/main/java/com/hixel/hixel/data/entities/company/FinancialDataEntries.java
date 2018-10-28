package com.hixel.hixel.data.entities.company;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

/**
 * Db Entity that holds the year of the entry, a list of Ratios for that year
 * (to be re-implemented), and the cik of the Company that is responsible for
 * the entries.
 */
@Entity(
        tableName = "data_entries",
        foreignKeys = @ForeignKey(
            entity = Identifiers.class, parentColumns = "cik", childColumns = "cik"
        ),
        indices = @Index(value = "cik", name = "cik")
)
public class FinancialDataEntries {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String cik;
    @SerializedName("year")
    private int year;

    //@Embedded
    //@TypeConverters(RatioConverter.class)
    //private List<Ratios> ratios;


    public String getCik() {
        return cik;
    }

    public int getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCik(String cik) {
        this.cik = cik;
    }
}
