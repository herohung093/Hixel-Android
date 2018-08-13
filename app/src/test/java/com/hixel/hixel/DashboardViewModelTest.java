package com.hixel.hixel;

import android.arch.lifecycle.MutableLiveData;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.viewmodel.DashboardViewModel;
import java.util.List;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DashboardViewModelTest {

    @Test
    public void testGetPortfolio() {
        DashboardViewModel dashboardViewModel = new DashboardViewModel();



        dashboardViewModel.getPortfolio();


    }
}
