package com.hixel.hixel;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.List;

public class MainGraphDataSet extends BarDataSet {

    public MainGraphDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
    }

    @Override
    public int getColor(int index) {
        if (getEntryForIndex(index).getY() < 0.5) {
            return mColors.get(2);
        } else if (getEntryForIndex(index).getY() > 0.6) {
            return mColors.get(0);
        } else {
            return mColors.get(1);
        }
    }
}
