package com.hixel.hixel.data.entities.company;

import org.junit.Test;

import static org.junit.Assert.*;

public class CompanyIdentifiersTest {

    @Test
    public void shouldReturnFormattedName() {
        Identifiers identifier = new Identifiers("cik", "ABC INC", "XYZ");

        assertEquals("Abc", identifier.getFormattedName());
    }

    @Test
    public void shouldReturnFormattedTicker() {
        Identifiers identifier = new Identifiers("cik", "ABC INC", "XYZ");

        assertEquals("NASDAQ: XYZ", identifier.getFormattedTicker());
    }
}
