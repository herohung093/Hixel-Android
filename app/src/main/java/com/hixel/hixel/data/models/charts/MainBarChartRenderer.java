package com.hixel.hixel.data.models.charts;

import android.graphics.Canvas;
import android.graphics.RectF;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Custom class for rendering BarCharts with our color palette. This class is highly extensible
 * and removes boilerplate from other views.
 */
public class MainBarChartRenderer extends BarChartRenderer {

    /**
     * Constructor to generate the chart
     *
     * @param chart The current BarChart.
     * @param animator To handle animations of the chart.
     * @param viewPortHandler The current View the chart is sitting in.
     */
    public MainBarChartRenderer(BarDataProvider chart, ChartAnimator animator,
            ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    /**
     * Create a rectangle for the bar chart shadow.
     */
    private RectF barShadowRectBuffer = new RectF();

    /**
     *
     * @param c The Canvas to draw the graph upon.
     * @param dataSet The values for the grpah.
     * @param index The current value needing to be drawn.
     */
    protected void drawDataSet(Canvas c, IBarDataSet dataSet, int index) {

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        mBarBorderPaint.setColor(dataSet.getBarBorderColor());
        mBarBorderPaint.setStrokeWidth(Utils.convertDpToPixel(dataSet.getBarBorderWidth()));

        final boolean drawBorder = dataSet.getBarBorderWidth() > 0.f;

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        // draw the bar shadow before the values
        if (mChart.isDrawBarShadowEnabled()) {
            mShadowPaint.setColor(dataSet.getBarShadowColor());

            BarData barData = mChart.getBarData();

            final float barWidth = barData.getBarWidth();
            final float barWidthHalf = barWidth / 2.0f;
            float x;

            for (int i = 0, count =
                    Math.min((int) (Math.ceil((float) (dataSet.getEntryCount()) * phaseX)),
                    dataSet.getEntryCount());
                    i < count;
                    i++) {

                BarEntry e = dataSet.getEntryForIndex(i);

                x = e.getX();

                barShadowRectBuffer.left = x - barWidthHalf;
                barShadowRectBuffer.right = x + barWidthHalf;

                trans.rectValueToPixel(barShadowRectBuffer);

                if (!mViewPortHandler.isInBoundsLeft(barShadowRectBuffer.right))
                    continue;

                if (!mViewPortHandler.isInBoundsRight(barShadowRectBuffer.left))
                    break;

                barShadowRectBuffer.top = mViewPortHandler.contentTop();
                barShadowRectBuffer.bottom = mViewPortHandler.contentBottom();

                c.drawRect(barShadowRectBuffer, mShadowPaint);
            }
        }

        // Initialize the buffer
        BarBuffer buffer = mBarBuffers[index];
        buffer.setPhases(phaseX, phaseY);
        buffer.setDataSet(index);
        buffer.setInverted(mChart.isInverted(dataSet.getAxisDependency()));
        buffer.setBarWidth(mChart.getBarData().getBarWidth());

        buffer.feed(dataSet);

        trans.pointValuesToPixel(buffer.buffer);

        final boolean isSingleColor = dataSet.getColors().size() == 1;

        if (isSingleColor) {
            mRenderPaint.setColor(dataSet.getColor());
        }

        for (int j = 0; j < buffer.size(); j += 4) {

            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2]))
                continue;

            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j]))
                break;

            if (!isSingleColor) {
                // Set the color for the currently drawn value. If the index
                // is out of bounds, reuse colors.
                mRenderPaint.setColor(dataSet.getColor(j / 4));
            }

            c.drawRoundRect(new RectF(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                    buffer.buffer[j + 3]), 8, 8, mRenderPaint);

            if (drawBorder) {
                c.drawRoundRect(new RectF(buffer.buffer[j],
                        buffer.buffer[j + 1],
                        buffer.buffer[j + 2],
                        buffer.buffer[j + 3]),
                        8,
                        8,
                        mBarBorderPaint);
            }
        }
    }

}
