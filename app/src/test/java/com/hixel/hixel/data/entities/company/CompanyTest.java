package com.hixel.hixel.data.entities.company;

import static org.junit.Assert.*;

import java.util.Collections;
import org.junit.Test;

public class CompanyTest {

    @Test
    public void shouldCreateCompleteCompany() {
        Company company = new Company();
        Identifiers identifier = new Identifiers("cik", "ABC INC", "XYZ");
        FinancialDataEntries dataEntries = new FinancialDataEntries(0, 0,
                "", new Ratios(1.0, 1.0, 1.0, 1.0));

        company.setDataEntries(Collections.singletonList(dataEntries));
        company.setIdentifiers(identifier);


        assertEquals(dataEntries, company.getDataEntries().get(0));
        assertEquals(identifier, company.getIdentifiers());
    }
}