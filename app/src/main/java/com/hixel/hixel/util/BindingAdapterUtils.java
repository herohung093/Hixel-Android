package com.hixel.hixel.util;

import android.databinding.BindingAdapter;
import android.view.View;

public class BindingAdapterUtils {
    @BindingAdapter({"isVisible"})
    public static void setIsVisible(View view, boolean isVisible) {
        if (isVisible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
