package com.hixel.hixel.data.entities.company;

import static org.junit.Assert.*;

import org.junit.Test;

public class FinancialDataEntriesTest {


    @Test
    public void shouldReturnCorrectOverallScore() {
        FinancialDataEntries dataEntries = new FinancialDataEntries(0, 0,
                "", new Ratios(1.0, 1.0, 1.0, 1.0));
        assertEquals(48.0, dataEntries.overallScore(), 0.01);
    }

    @Test
    public void shouldReturnCorrectReturns() {
        FinancialDataEntries dataEntries = new FinancialDataEntries(0, 0,
                "", new Ratios(1.0, 1.0, 1.0, 1.0));
        assertEquals(1, dataEntries.getReturns());
    }

    @Test
    public void shouldReturnCorrectPerformance() {
        FinancialDataEntries dataEntries = new FinancialDataEntries(0, 0,
                "", new Ratios(1.0, 1.0, 1.0, 1.0));
        assertEquals(1, dataEntries.getReturns());
    }

    @Test
    public void shouldReturnCorrectStrength() {
        FinancialDataEntries dataEntries = new FinancialDataEntries(0, 0,
                "", new Ratios(1.0, 1.0, 1.0, 1.0));
        assertEquals(1, dataEntries.getReturns());
    }

    @Test
    public void shouldReturnCorrectHealth() {
        FinancialDataEntries dataEntries = new FinancialDataEntries(0, 0,
                "", new Ratios(1.0, 1.0, 1.0, 1.0));
        assertEquals(1, dataEntries.getReturns());
    }

    @Test
    public void shouldReturnCorrectSafety() {
        FinancialDataEntries dataEntries = new FinancialDataEntries(0, 0,
                "", new Ratios(1.0, 1.0, 1.0, 1.0));
        assertEquals(1, dataEntries.getReturns());
    }
}