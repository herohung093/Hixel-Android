package com.hixel.hixel.deprecatedTests;

//Normally I wouldn't comment out a whole file like this, but it's throwing all kinds of nasty warnings up.

/*
import static net.bytebuddy.matcher.ElementMatchers.is;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;

import com.hixel.hixel.data.database.AppDatabase;
import com.hixel.hixel.data.database.IdentifiersDao;
import com.hixel.hixel.data.entities.company.Company;
import com.hixel.hixel.data.entities.company.Identifiers;
import com.hixel.hixel.ui.dashboard.DashboardViewModel;
import io.reactivex.Observer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CompanyDataTest {

    @Test
    public void testSetDefaultFinancialData() {
       /* Company companyData = new Company(0, new LinkedHashMap<>());

        companyData.setDefaultFinancialData();

        LinkedHashMap<String, Double> defaultFinancialData = new LinkedHashMap<>();
        defaultFinancialData.put("Current Ratio", 0.0);
        defaultFinancialData.put("Quick Ratio", 0.0);
        defaultFinancialData.put("Cash Ratio", 0.0);
        defaultFinancialData.put("Dept-to-Equity Ratio", 0.0);
        defaultFinancialData.put("Health", 0.0);
        defaultFinancialData.put("Long_Term_Debt_Ratio", 0.0);

        assertEquals("Default values should all be 0.0", companyData.getRatios(), defaultFinancialData);*/
    }

    @Test
    public void testSetYear() {
        /*Company companyData = new CompanyData(0, new LinkedHashMap<>());

        companyData.setYear(7);

        assertEquals("Year should equal 7", companyData.getYear(), 7);*/
    }

    public static class CompanyTest {

        @Test
        public void testGetRatio() {
           /* Company company = new Company(new CompanyIdentifiers("AAPL", "APPLE", "CIK"), new ArrayList<>());

            for (CompanyData f : company.getFinancialDataEntries()) {
                f.setYear(1);
                f.setDefaultFinancialData();
            }

            assertEquals("Should return 0.0", company.getRatio("Health", 1), 0.0);

        }*/

    }

    public static class DashboardViewModelTest {

        // Run on test thread
        @Rule
        public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

        @Mock
        private Observer<ArrayList<Company>> observer;

        private DashboardViewModel dashboardViewModel;


        @Before
        public void setupDashboardViewModel() {
            // Inject mocks
            MockitoAnnotations.initMocks(this);

            // Get a reference to the class under test
            // dashboardViewModel = new DashboardViewModel();
        }

        @Test
        public void testPortfolioLoad() {
            /*ArrayList<Company> result = new ArrayList<>();

            // If getPortfolio is called
            observer.onChanged(result);
            dashboardViewModel.getPortfolio().observeForever(observer);

            // Then there should be data in the results array.
            Assert.assertNotEquals(null, result);*/
        }
    }

    public static class IdentifiersDaoTest {
        @Rule
        public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

        private AppDatabase database;
        private IdentifiersDao identifiersDao;

        @Before
        public void initDb() throws Exception {
<<<<<<< HEAD
            database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase.class)
                           .allowMainThreadQueries()
                           .build();
=======
            //database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
            //        AppDatabase.class)
            //        .allowMainThreadQueries()
            //        .build();
>>>>>>> origin

            identifiersDao = database.identifiersDao();
        }

        @After
        public void close() throws Exception {
            database.close();
        }

        @Test
        public void getIdentifiersAfterInserted() throws InterruptedException {
           /* Identifiers identifiers = new Identifiers();
            identifiers.setCik("abc");
            identifiers.setName("xyz");
            identifiers.setTicker("AAPL");

            Company company = new Company();
            company.getIdentifiers().setCik("1234");
            company.getIdentifiers().setTicker("AAPL");
            company.getIdentifiers().setName("Apple");

            ArrayList<Company> companies = new ArrayList<>();
            companies.add(company);
            companies.add(company);
            companies.add(company);

            identifiersDao.insertCompanies(companies);

            List<Company> dbResponse = LiveDataTestUtil.getValue(identifiersDao.loadCompanies());

            Assert.assertEquals(dbResponse.size(), is(3));*/
        }
    }
<<<<<<< HEAD
}
*/
=======
}}
>>>>>>> origin
