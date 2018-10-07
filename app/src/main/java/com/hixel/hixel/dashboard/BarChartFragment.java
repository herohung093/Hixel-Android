package com.hixel.hixel.dashboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.hixel.hixel.R;
import com.hixel.hixel.data.models.MainBarChartRenderer;
import com.hixel.hixel.data.models.MainBarDataSet;
import java.util.ArrayList;
import java.util.List;


public class BarChartFragment extends Fragment {

    private BarChart chart;

    public BarChartFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bar_chart, container, false);

        chart = view.findViewById(R.id.chart);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupChart();
        updateChart();
    }

    private void updateChart() {
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    private void setupChart() {
        List<BarEntry> entries = new ArrayList<>();
        MainBarDataSet dataSet = new MainBarDataSet(entries, "");

        chart.setRenderer(new MainBarChartRenderer(chart, chart.getAnimator(), chart.getViewPortHandler()));
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setDrawValueAboveBar(false);
        chart.setDrawBarShadow(false);

        ArrayList<String> labels = new ArrayList<>();
        labels.add("Health");
        labels.add("Performance");
        labels.add("Return");
        labels.add("Safety");
        labels.add("Strength");

        int[] colours = {
                ContextCompat.getColor(getContext(), R.color.good),
                ContextCompat.getColor(getContext(), R.color.average),
                ContextCompat.getColor(getContext(), R.color.bad)
        };

        dataSet.setColors(colours);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.2f);
        data.setDrawValues(false);


        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_secondary_dark));
        xAxis.setTextSize(12);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(5);
        xAxis.setValueFormatter((value, axis) -> labels.get((int) value));

        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_secondary_dark));
        yAxisLeft.setPosition(YAxisLabelPosition.OUTSIDE_CHART);
        yAxisLeft.setDrawGridLines(true);
        yAxisLeft.setTextSize(12);
        yAxisLeft.setAxisMaximum(5.0f);
        yAxisLeft.setAxisMinimum(0.0f);
        yAxisLeft.setGranularity(1f); // set interval
        yAxisLeft.setDrawLabels(true);
        yAxisLeft.setDrawAxisLine(false);
        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setEnabled(false);

        chart.setData(data);
        chart.invalidate();
    }
}
