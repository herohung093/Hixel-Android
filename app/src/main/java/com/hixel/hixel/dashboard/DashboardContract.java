package com.hixel.hixel.dashboard;

import com.hixel.hixel.BasePresenter;
import com.hixel.hixel.BaseView;

public interface DashboardContract {

    interface View extends BaseView<Presenter> {
        void showMainGraph();
    }

    interface Presenter extends BasePresenter {

    }
}
