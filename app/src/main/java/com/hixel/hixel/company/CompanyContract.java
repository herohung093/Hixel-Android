package com.hixel.hixel.company;

import com.hixel.hixel.BasePresenter;
import com.hixel.hixel.BaseView;

public interface CompanyContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        String getCompanyName();
    }
}
