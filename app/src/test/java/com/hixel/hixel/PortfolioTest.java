package com.hixel.hixel;

import com.hixel.hixel.data.Company;
import com.hixel.hixel.data.Portfolio;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

public class PortfolioTest {

    @Test
    public void testCreation() {
        ArrayList<Company>  companies = new ArrayList<>();
        companies.add(new Company("X", "X", 0.7));

        Portfolio p = new Portfolio(companies);

        assertEquals("Should return companies", p.getCompanies(), companies);
    }

    @Test
    public void getIndividualCompany() {
        ArrayList<Company>  companies = new ArrayList<>();
        companies.add(new Company("X", "X", 0.7));
        Portfolio p = new Portfolio(companies);

        assertEquals("Should return company at index", p.getCompany(0), companies.get(0));

    }

}
