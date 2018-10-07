package com.hixel.hixel;

import static junit.framework.Assert.assertEquals;

import com.hixel.hixel.data.entities.FinancialData;
import java.util.LinkedHashMap;
import org.junit.Test;

public class FinancialDataTest {

    @Test
    public void testSetDefaultFinancialData() {
        FinancialData financialData = new FinancialData(0, new LinkedHashMap<>());

        financialData.setDefaultFinancialData();

        LinkedHashMap<String, Double> defaultFinancialData = new LinkedHashMap<>();
        defaultFinancialData.put("Current Ratio", 0.0);
        defaultFinancialData.put("Quick Ratio", 0.0);
        defaultFinancialData.put("Cash Ratio", 0.0);
        defaultFinancialData.put("Dept-to-Equity Ratio", 0.0);
        defaultFinancialData.put("Health", 0.0);
        defaultFinancialData.put("Long_Term_Debt_Ratio", 0.0);

        assertEquals("Default values should all be 0.0", financialData.getRatios(), defaultFinancialData);
    }

    @Test
    public void testSetYear() {
        FinancialData financialData = new FinancialData(0, new LinkedHashMap<>());

        financialData.setYear(7);

        assertEquals("Year should equal 7", financialData.getYear(), 7);
    }

}
