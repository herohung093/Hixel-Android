package com.hixel.hixel.ui.companycomparison;

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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.hixel.hixel.R;
import com.hixel.hixel.data.entities.company.Company;
import java.util.ArrayList;
import java.util.List;


public class GenericChartFragment extends Fragment {

    private RadarChart radarChart;
    ArrayList<Integer> colors =new ArrayList<>();
    public GenericChartFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        colors.add(Color.rgb(255,218,185));
        colors.add(Color.rgb(139,136,120));
        colors.add(Color.rgb(208,32,144));
        colors.add(Color.rgb(193,205,193));
        colors.add(Color.rgb(230,230,250));
        colors.add(Color.rgb(100,149,237));
        colors.add(Color.rgb(106,90,205));
        colors.add(Color.rgb(0,255,127));
        colors.add(Color.rgb(255,215,0));
        colors.add(Color.rgb(205,92,92));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_generic_chart, container, false);
        radarChart = view.findViewById(R.id.radarChart);
        return view;
    }

    private RadarDataSet radarDataSetup(Company company){
        ArrayList<RadarEntry> entries= new ArrayList<>();

        entries.add(new RadarEntry(company.getDataEntries().get(0).getReturns(),1));
        entries.add(new RadarEntry(company.getDataEntries().get(0).getPerformance(),2));
        entries.add(new RadarEntry(company.getDataEntries().get(0).getStrength(),3));
        entries.add(new RadarEntry(company.getDataEntries().get(0).getHealth(),4));
        entries.add(new RadarEntry(company.getDataEntries().get(0).getSafety(),5));

        RadarDataSet dataSet = new RadarDataSet(entries,company.getIdentifiers().getTicker());
        return dataSet;
    }
    public void drawGraph(List<Company> companies) {

        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        ArrayList<RadarDataSet> radarDataSets = new ArrayList<>();
        for(Company c: companies){
            radarDataSets.add(radarDataSetup(c));
        }
        setupDataSetStyle(radarDataSets);
        sets.addAll(radarDataSets);

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
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

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
        radarChart.setExtraOffsets(0f, 10f, 0f, 5f);
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
    private void setupDataSetStyle(ArrayList<RadarDataSet> sets){
        for(int i=0; i< sets.size();i++){
            sets.get(i).setColor(colors.get(i));
            sets.get(i).setFillColor(colors.get(i));
            sets.get(i).setDrawFilled(true);
            sets.get(i).setFillAlpha(90);
            sets.get(i).setLineWidth(1f);
            sets.get(i).setDrawHighlightCircleEnabled(true);
            sets.get(i).setDrawHighlightIndicators(false);
            sets.get(i).setValueTextColor(Color.rgb(60, 220, 78));
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
