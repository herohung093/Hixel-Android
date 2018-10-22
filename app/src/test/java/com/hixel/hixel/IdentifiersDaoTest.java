package com.hixel.hixel;


import static net.bytebuddy.matcher.ElementMatchers.is;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import com.hixel.hixel.data.database.AppDatabase;
import com.hixel.hixel.data.database.IdentifiersDao;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.entities.Identifiers;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.*;

public class IdentifiersDaoTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule()();

    private AppDatabase database;
    private IdentifiersDao identifiersDao;

    @Before
    public void initDb() throws Exception {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext()),
                AppDatabase.class)
                .allowMainThreadQueries()
                .build();

        identifiersDao = database.identifiersDao();
    }

    @After
    public void close() throws Exception {
        database.close();
    }

    @Test
    public void getIdentifiersAfterInserted() throws InterruptedException {
        Identifiers identifiers = new Identifiers();
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

        Assert.assertEquals(dbResponse.size(), is(3));
    }
}
