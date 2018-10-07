package com.hixel.hixel;

import static junit.framework.Assert.assertEquals;

import com.hixel.hixel.data.entities.CompanyData;
import java.util.LinkedHashMap;
import org.junit.Test;

public class CompanyDataTest {

    @Test
    public void testSetDefaultFinancialData() {
        CompanyData companyData = new CompanyData(0, new LinkedHashMap<>());

        companyData.setDefaultFinancialData();

        LinkedHashMap<String, Double> defaultFinancialData = new LinkedHashMap<>();
        defaultFinancialData.put("Current Ratio", 0.0);
        defaultFinancialData.put("Quick Ratio", 0.0);
        defaultFinancialData.put("Cash Ratio", 0.0);
        defaultFinancialData.put("Dept-to-Equity Ratio", 0.0);
        defaultFinancialData.put("Health", 0.0);
        defaultFinancialData.put("Long_Term_Debt_Ratio", 0.0);

        assertEquals("Default values should all be 0.0", companyData.getRatios(), defaultFinancialData);
    }

    @Test
    public void testSetYear() {
        CompanyData companyData = new CompanyData(0, new LinkedHashMap<>());

        companyData.setYear(7);

        assertEquals("Year should equal 7", companyData.getYear(), 7);
    }

}
