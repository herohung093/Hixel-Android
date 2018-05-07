package com.hixel.hixel.dashboard;

import com.hixel.hixel.BasePresenter;
import com.hixel.hixel.BaseView;
import com.hixel.hixel.data.Company;
import java.util.ArrayList;

public interface DashboardContract {

    interface View extends BaseView<Presenter> {
        void showMainGraph(ArrayList<Company> companies);
    }

    interface Presenter extends BasePresenter {
        void populateGraph();
        ArrayList<Company> getCompanies();
        int setHealthColor(int position);
    }
}
