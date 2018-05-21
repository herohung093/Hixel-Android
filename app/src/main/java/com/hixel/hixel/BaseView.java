package com.hixel.hixel;

import com.google.gson.internal.bind.ArrayTypeAdapter;

import java.util.ArrayList;

public interface BaseView<T> {

    void setPresenter(T presenter);
    void updateRatios(ArrayList<String>ratios1);
}
