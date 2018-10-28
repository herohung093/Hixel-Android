package com.hixel.hixel.data.entities.company;

import android.arch.persistence.room.ColumnInfo;
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
@Entity(foreignKeys = {
                @ForeignKey(
                    entity = Identifiers.class,
                    parentColumns = "id",
                    childColumns = "identifier_id"
        )}//,
        //indices = @Index(value = "identifier_id", name = "id")
)
public class FinancialDataEntries {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("year")
    @ColumnInfo(name="year")
    public int year;


    @ColumnInfo(name="identifier_id")
    public String identifierId;
}
