package com.hixel.hixel;

import java.util.ArrayList;

public interface BaseView<T> {

    void setPresenter(T presenter);
    void updateRatios(ArrayList<String>ratios1);
}
