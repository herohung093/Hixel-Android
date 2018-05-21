package com.hixel.hixel.models;

import com.hixel.hixel.company.CompanyIdentifiers;
import com.hixel.hixel.company.FinancialData;

import java.io.Serializable;
import java.util.List;

// Implementing serializable while we can to prevent coupling with the android SDK
public class Company implements Serializable {
    private CompanyIdentifiers identifiers;
    private List<FinancialData> financialDataEntries;


    public Company(CompanyIdentifiers identifiers, List<FinancialData> financialDataEntries) {
        this.identifiers = identifiers;
        this.financialDataEntries = financialDataEntries;
    }

    //NOTE: This function never returns null.
    public Double getRatio(String name, int year) {
        Double ratio = null;

        for (FinancialData entry : financialDataEntries) {
            if (entry.getYear() == year) {
                ratio = entry.getRatios().get(name);
            }
        }

        return (ratio != null) ? ratio : 0.0;
    }

    public CompanyIdentifiers getIdentifiers() {
        return identifiers;
    }

    public List<FinancialData> getFinancialDataEntries() {
        return financialDataEntries;
    }

}
