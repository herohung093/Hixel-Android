package com.hixel.hixel;

import static junit.framework.Assert.assertEquals;

import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.service.models.CompanyIdentifiers;
import com.hixel.hixel.data.entities.FinancialData;
import java.util.ArrayList;
import org.junit.Test;

public class CompanyTest {

    @Test
    public void testGetRatio() {
        Company company = new Company(new CompanyIdentifiers("AAPL", "APPLE", "CIK"), new ArrayList<>());

        for (FinancialData f : company.getFinancialDataEntries()) {
            f.setYear(1);
            f.setDefaultFinancialData();
        }

        assertEquals("Should return 0.0", company.getRatio("Health", 1), 0.0);

    }

}
