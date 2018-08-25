package com.hixel.hixel.view.ui;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.hixel.hixel.R;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.service.models.FinancialData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class GraphFragment extends Fragment {

    LineChart lineChart;
    String[] years;
    ArrayList<Integer> compAColors= new ArrayList<Integer>();
    ArrayList<Integer> compBColors= new ArrayList<Integer>();
    private OnFragmentInteractionListener mListener;

    public GraphFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        lineChart = view.findViewById(R.id.chart1);
        return view;
    }
    public LineDataSet lineChartDataSetup( String selectedRatio, Company company){
        List <Entry> compEntry = new ArrayList<>();
        List<FinancialData> financialData= company.getFinancialDataEntries();
        ArrayList<Float> rawData= new ArrayList<>();
        checkYearNull(financialData);

        createListOfYears(financialData);
        int j=4;
        for (int i=0;i<5;i++){
            LinkedHashMap<String, Double> DataCompAYear1 = financialData.get(j).getRatios();
            j--;
            Entry compYearData = new Entry(i, Float.valueOf(DataCompAYear1.get(selectedRatio).toString()));
            rawData.add(Float.valueOf(DataCompAYear1.get(selectedRatio).toString()));
            compEntry.add(compYearData);
        }

        return new LineDataSet(compEntry,company.getIdentifiers().getTicker());
    }
    public void colorIndicator(Company company, String selectedRatio, ArrayList<Integer> colors){
        ArrayList<Float> rawData= new ArrayList<>();
        ArrayList<Float> sortedData= new ArrayList<>();
        List<FinancialData> financialData= company.getFinancialDataEntries();
        checkYearNull(financialData);
        int j=4;
        for (int i=0;i<5;i++){
            LinkedHashMap<String, Double> DataCompAYear1 = financialData.get(j).getRatios();
            j--;
            rawData.add(Float.valueOf(DataCompAYear1.get(selectedRatio).toString()));
            colors.add(0);
        }
        sortedData.addAll(rawData);
        Collections.sort(sortedData);
        for(int i=1;i<rawData.size();i++){
            if(rawData.get(i)==sortedData.get(0))
                colors.add(i-1,getResources().getColor(R.color.bad));
            else
            if(rawData.get(i)>rawData.get(i-1)){
                colors.add(i-1,getResources().getColor(R.color.good));
            }else
            if (rawData.get(i)<rawData.get(i-1)){
                colors.add(i-1,getResources().getColor(R.color.bad));
            }else colors.add(i-1,getResources().getColor(R.color.average));
        }

    }
    public void drawGraph(ArrayList<Company> companies,String selectedRatio){
        List<ILineDataSet> dataSets = new ArrayList<>();

        for(Company c: companies){
            LineDataSet setCompA= lineChartDataSetup( selectedRatio, c);
            if (companies.size()==1) {
                colorIndicator(c,selectedRatio,compAColors);
                setCompA.setColors(compAColors);
                setupDatasetStyle(setCompA,companies.get(0).getFinancialDataEntries());
                setCompA.enableDashedLine(10f, 10f, 10f);
                dataSets.add(setCompA);
            } else if(companies.size()==2 && companies.indexOf(c)==0) {
                colorIndicator(c,selectedRatio,compAColors);
                setCompA.setColors(compAColors);
                setupDatasetStyle(setCompA,companies.get(0).getFinancialDataEntries());
                setCompA.enableDashedLine(10f, 10f, 10f);
                dataSets.add(setCompA);
            } else {
                colorIndicator(c,selectedRatio,compBColors);
                setCompA.setColors(compBColors);
                setupDatasetStyle(setCompA,companies.get(1).getFinancialDataEntries());
                dataSets.add(setCompA);
            }

        }
        Description description=lineChart.getDescription();
        description.setEnabled(false);
        LineData data = new LineData(dataSets);
        decorLineChart(lineChart,data);
    }

    public void decorLineChart(LineChart lineChart, LineData data){

        lineChart.animateXY(1000, 1000);
        lineChart.setData(data);
        lineChart.invalidate();
        XAxis xAxis = lineChart.getXAxis();

        YAxis yAxis = lineChart.getAxisLeft();
        YAxis yRight = lineChart.getAxisRight();
        yRight.setTextColor(R.color.text_main_light);
        setupAxis(xAxis, yAxis);

        Legend legend = lineChart.getLegend();
        setupLegend(legend);
    }


    IAxisValueFormatter formatter = new IAxisValueFormatter() {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return years[(int) value];
        }

    };

    private void checkYearNull(List<FinancialData> financial) {
        for (int i = 0; i < financial.size(); i++) {
            if (financial.get(i) == null) {
                financial.get(i).setDefaultFinancialData(); //set all values equal to -0 for visualising purpose
                financial.get(i).setYear(financial.get(i - 1).getYear() - 1);
            }
        }
    }

    private void createListOfYears(List<FinancialData> financialDataCompA) {
        List<String> toConvertYears = new ArrayList<>();

        for (int i = financialDataCompA.size() - 1; i >= 0; i--) {
            toConvertYears.add(String.valueOf(financialDataCompA.get(i).getYear()));
        }

        years = toConvertYears.toArray(new String[toConvertYears.size()]);
    }

    public void setupDatasetStyle(LineDataSet setCompA, List<FinancialData> financialData) {
        setCompA.setDrawCircleHole(true);
        setCompA.setValueTextSize(12);
        setCompA.setValueTextColor(Color.WHITE);

        setCompA.setCircleHoleRadius(3);
        setCompA.setAxisDependency(YAxis.AxisDependency.LEFT);

    }

    public void setupLegend(Legend legend) {
        legend.setEnabled(true);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setFormSize(7f); // set the size of the legend forms/shapes
        legend.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        // TODO: Check if deprecated method is required
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);

        legend.setTextSize(14f);
        legend.setTextColor(Color.WHITE);
        legend.setXEntrySpace(55); // set the space between the legend entries on the x-axis
        legend.setYEntrySpace(5f); // set the space between the legend entries on the y-axis
        // set custom labels and colors
    }

    public void setupAxis(XAxis xAxis, YAxis yAxis) {
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.WHITE);

        yAxis.setTextSize(12f);
        yAxis.setTextColor(Color.WHITE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        // void onFragmentInteraction(Uri uri);
    }
}
