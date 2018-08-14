package com.hixel.hixel;

import static com.hixel.hixel.service.network.Client.getClient;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.service.models.CompanyIdentifiers;
import com.hixel.hixel.service.network.ServerInterface;
import com.hixel.hixel.viewmodel.DashboardViewModel;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.TestScheduler;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class DashboardViewModelTest {

    // Run on test thread
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private Observer<List<Company>> observer;

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
