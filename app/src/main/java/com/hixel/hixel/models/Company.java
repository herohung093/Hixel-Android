package com.hixel.hixel.models;

import com.hixel.hixel.company.CompanyIdentifiers;
import com.hixel.hixel.company.FinancialData;

import java.io.Serializable;
import java.util.ArrayList;

// Implementing serializable while we can to prevent coupling with the android SDK
public class Company implements Serializable {
    private CompanyIdentifiers identifiers;
    private ArrayList<FinancialData> financialDataEntries;


    public Company(CompanyIdentifiers identifiers, ArrayList<FinancialData> financialDataEntries) {
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

        return (ratio != null) ? ratio
                               : 0.0;
    }

    public CompanyIdentifiers getIdentifiers() {
        return identifiers;
    }

    public ArrayList<FinancialData> getFinancialDataEntries() {
        return financialDataEntries;
    }

}
