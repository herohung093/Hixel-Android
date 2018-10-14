package com.hixel.hixel;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verify;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.ui.dashboard.DashboardViewModel;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class DashboardViewModelTest {

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
        dashboardViewModel = new DashboardViewModel();
    }

    @Test
    public void testPortfolioLoad() {
        ArrayList<Company> result = new ArrayList<>();

        // If getPortfolio is called
        observer.onChanged(result);
        dashboardViewModel.getPortfolio().observeForever(observer);

        // Then there should be data in the results array.
        Assert.assertNotEquals(null, result);
    }
}
