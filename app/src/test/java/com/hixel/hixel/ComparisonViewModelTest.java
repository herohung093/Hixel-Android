package com.hixel.hixel;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.viewmodel.ComparisonViewModel;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ComparisonViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private Observer<ArrayList<Company>> observer;
    private ComparisonViewModel comparisonViewModel;

    @Before
    public void initialSetup(){
        MockitoAnnotations.initMocks(this);
        comparisonViewModel= new ComparisonViewModel();
    }

    @Test
    public void companiesLoad(){
        ArrayList<Company> companies = new ArrayList<>();

        observer.onChanged(companies);

        comparisonViewModel.getCompanies().observeForever(observer);

        Assert.assertNotEquals(null,companies);
    }

    @Test
    public void loadDashboardCompanies(){
        ArrayList<Company> dashboardCompanies= new ArrayList<>();
        observer.onChanged(dashboardCompanies);
        comparisonViewModel.getPortfolio().observeForever(observer);

        Assert.assertNotEquals(null,dashboardCompanies);
    }
}
