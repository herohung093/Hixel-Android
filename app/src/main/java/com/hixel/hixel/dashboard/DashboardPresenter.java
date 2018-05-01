package com.hixel.hixel.dashboard;

import android.support.annotation.NonNull;

public class DashboardPresenter implements DashboardContract.Presenter {
    // TODO: Add model
    private final DashboardContract.View mDashboardView;


    public DashboardPresenter(DashboardContract.View dashboardView) {
        mDashboardView = dashboardView;
        mDashboardView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
