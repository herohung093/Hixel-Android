package com.hixel.hixel;


import com.hixel.hixel.service.models.SearchEntry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SearchEntryTest {

    // Simple test to assure the class returns things we want it to.
    @Test
    public void testSearchClassCreation() {
        SearchEntry searchEntry = new SearchEntry("ABC", "Apple", "NASDAQ");

        assertEquals("Ticker value is incorrect", "ABC", searchEntry.getTicker());
        assertEquals("Name value is incorrect", "Apple", searchEntry.getName());
        assertEquals("Exchange value is incorrect", "NASDAQ", searchEntry.getExchange());
    }



}
