package com.hixel.hixel.data.models.charts;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.List;

/**
 * Generates a custom BarDataSet with colors from our palette.
 */
public class MainBarDataSet extends BarDataSet {

    public MainBarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
    }

    /**
     * Takes in the value and generates a corresponding color.
     *
     * @param index which graph we are coloring.
     * @return returns a color.
     */
    @Override
    public int getColor(int index) {
        // NOTE: Leave this as is, will be adding gradients later.
        if (getEntryForIndex(index).getY() < 3) {
            return mColors.get(2);
        } else if (getEntryForIndex(index).getY() > 3) {
            return mColors.get(0);
        } else {
            return mColors.get(1);
        }
    }
}
