package com.hixel.hixel.data.entities.company;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * Company POJO containing all the identifying information for a company.
 * The Identifiers and FinancialDataEntries are both Entities, and are stored in the db,
 * this class acts as a wrapper so we have less queries to make and nicer looking code.
 */
public class Company implements Serializable {
    @Embedded
    @SerializedName("identifiers")
    private Identifiers identifiers;

    @Relation(
            parentColumn = "id",
            entityColumn = "identifier_id",
            entity = FinancialDataEntries.class
    )
    @SerializedName("financialDataEntries")
    private List<FinancialDataEntries> dataEntries;

    public Identifiers getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(Identifiers identifiers) {
        this.identifiers = identifiers;
    }

    public List<FinancialDataEntries> getDataEntries() {
        return dataEntries;
    }

    public void setDataEntries(List<FinancialDataEntries> dataEntries) {
        this.dataEntries = dataEntries;
    }
}
