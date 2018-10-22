package com.hixel.hixel.data.entities;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * Immutable Company Entity containing all the identifying information for a company.
 */
public class Company implements Serializable {
    @Embedded
    private Identifiers identifiers;

    @Ignore
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
