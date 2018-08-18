package com.hixel.hixel.service.models;

import android.graphics.Color;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.List;

public class MainBarDataSet extends BarDataSet{
    int[] colours = {
            Color.parseColor("#4BCA81"),    // good
            Color.parseColor("#FFB75D"),    // average
            Color.parseColor("#C23934")     // bad
    };

    public MainBarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
    }

    @Override
    public int getColor(int index) {
        // NOTE: Leave this as is, will be adding gradients later.
        if (getEntryForIndex(index).getY() < 3) {
            return colours[2];
        } else if (getEntryForIndex(index).getY() > 3) {
            return colours[0];
        } else {
            return colours[1];
        }
    }


}
