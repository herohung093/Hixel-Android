package com.hixel.hixel.companycomparison;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.hixel.hixel.R;
import com.hixel.hixel.data.entities.Company;
import java.util.ArrayList;


public class GenericChartFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RadarChart radarChart;

    public GenericChartFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_generic_chart, container, false);
        radarChart= view.findViewById(R.id.radarChart);

        return view;
    }

    public void drawGraph(ArrayList<Company> companies){

        ArrayList<RadarEntry> entries1= new ArrayList<>();

        entries1.add(new RadarEntry(5f, 1));
        entries1.add(new RadarEntry(2f, 2));
        entries1.add(new RadarEntry(1f, 3));
        entries1.add(new RadarEntry(3f, 4));
        entries1.add(new RadarEntry(5f, 5));

        ArrayList<RadarEntry> entries2 = new ArrayList<>();
        entries2.add(new RadarEntry(1f, 1));
        entries2.add(new RadarEntry(5f, 2));
        entries2.add(new RadarEntry(4f, 3));
        entries2.add(new RadarEntry(3f, 4));
        entries2.add(new RadarEntry(4f, 5));

        RadarDataSet dataSet_compA= null; //new RadarDataSet(entries1,companies.get(0).getIdentifiers().getName());
        RadarDataSet dataSet_compB= null; //new RadarDataSet(entries2,companies.get(1).getIdentifiers().getName());

        //set color
        dataSet_compA.setDrawFilled(true);
        dataSet_compA.setColor(Color.rgb(229, 13, 92));
        dataSet_compA.setFillColor(Color.rgb(229, 13, 92));
        dataSet_compA.setFillAlpha(90);
        dataSet_compA.setLineWidth(1f);
        dataSet_compA.setDrawHighlightCircleEnabled(true);
        dataSet_compA.setDrawHighlightIndicators(false);
        dataSet_compA.setValueTextColor(Color.GRAY);

        dataSet_compB.setColor(Color.rgb(47, 237, 208));
        dataSet_compB.setFillColor(Color.rgb(47, 237, 208));
        dataSet_compB.setDrawFilled(true);
        dataSet_compB.setFillAlpha(90);
        dataSet_compB.setLineWidth(1f);
        dataSet_compB.setDrawHighlightCircleEnabled(true);
        dataSet_compB.setDrawHighlightIndicators(false);
        dataSet_compB.setValueTextColor(Color.rgb(60, 220, 78));

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(dataSet_compA);
        sets.add(dataSet_compB);

        RadarData data = new RadarData(sets);

        data.setValueTextSize(10f);
        data.setDrawValues(true);
        data.setValueTextColor(Color.BLACK);
        data.setValueFormatter((value, entry , dataSetIndex, viewPortHandler) -> String.valueOf((int) value));

        Legend legend = radarChart.getLegend();
        legend.setEnabled(true);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setFormSize(9f); // set the size of the legend forms/shapes
        legend.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        legend.setPosition(LegendPosition.BELOW_CHART_CENTER);

        legend.setTextSize(14f);
        legend.setTextColor(Color.GRAY);
        legend.setXEntrySpace(30); // set the space between the legend entries on the x-axis
        //legend.setYEntrySpace(5f); // set the space between the legend entries on the y-axis

        radarChart.setData(data);
        radarChart.animateXY(1400, 1400);
        radarChart.setWebLineWidth(0.8f);
        radarChart.setWebColor(Color.LTGRAY);
        radarChart.setWebLineWidthInner(0.5f);
        radarChart.setWebColorInner(Color.LTGRAY);
        radarChart.setWebAlpha(100);
        //radarChart.setScaleX(1.1f);
        //radarChart.setScaleY(1.1f);
        radarChart.getDescription().setEnabled(false);
        radarChart.setExtraOffsets(0f, 10f, 0f, 0f);
        XAxis xAxis = radarChart.getXAxis();
        //xAxis.setTypeface(mTfLight);
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private String[] mActivities = new String[]{"Returns","Performance","Strength","Health","Safety"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        xAxis.setTextColor(Color.rgb(232, 163, 34));
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        YAxis yAxis = radarChart.getYAxis();

        yAxis.setLabelCount(5, true);

        yAxis.setDrawLabels(false);

        radarChart.invalidate();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    interface OnFragmentInteractionListener { }
}
