package com.hixel.hixel;

import com.hixel.hixel.models.Company;
import org.junit.Test;

public class CompanyActivityTest {

    @Test
    public void creation_isCorrect() {
        Company c = new Company("X", "X", 0.7);
        assertEquals("Ticker should be AAPL", c.getName(), "X");
        assertEquals("Ticker should be AAPL", c.getTicker(), "X");
        assertEquals("Ticker should be AAPL", c.getHealth(), 0.7, 0.1);
    }

}
