package com.hixel.hixel.view.ui;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.hixel.hixel.R;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.service.models.FinancialData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class GraphFragment extends Fragment {

    private CombinedChart mChart;
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
        mChart =  view.findViewById(R.id.chart1);
        return view;
    }
    public LineDataSet lineChartDataSetup( String selectedRatio, Company company){
        List <Entry> compEntry = new ArrayList<>();
        List<FinancialData> financialData= company.getFinancialDataEntries();
        checkYearNull(financialData);

        createListOfYears(financialData);
        int j=4;
        for (int i=0;i<5;i++){
            LinkedHashMap<String, Double> DataCompAYear1 = financialData.get(j).getRatios();
            j--;
            Entry compYearData = new Entry(i, Float.valueOf(DataCompAYear1.get(selectedRatio).toString()));
            compEntry.add(compYearData);
        }

        return new LineDataSet(compEntry,company.getIdentifiers().getTicker());
    }

    public BarDataSet barChartDataSetup(String selectedRatio, Company company){
        List<BarEntry> compEntry = new ArrayList<>();
        List<FinancialData> financialData= company.getFinancialDataEntries();
        checkYearNull(financialData);
        createListOfYears(financialData);
        int j=4;
        for (int i=0;i<5;i++){
            LinkedHashMap<String, Double> DataCompAYear1 = financialData.get(j).getRatios();
            j--;
            BarEntry compYearData = new BarEntry(i, Float.valueOf(DataCompAYear1.get(selectedRatio).toString()));
            compEntry.add(compYearData);
        }
        return new BarDataSet(compEntry,company.getIdentifiers().getTicker());
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

        LineData lineData = new LineData();
        LineDataSet setCompA= lineChartDataSetup( selectedRatio, companies.get(0));
        lineData.addDataSet(setCompA);
        LineDataSet setCompB = lineChartDataSetup(selectedRatio, companies.get(1));
        //BarDataSet setCompB = barChartDataSetup(selectedRatio, companies.get(1));
        //BarData barData = new BarData(setCompB);
        lineData.addDataSet(setCompB);
        ///barData.setBarWidth(0.45f);
        //colorIndicator(companies.get(0),selectedRatio,compAColors);

        setupDatasetStyle(setCompA,setCompB);//
        //draw graph
        mChart.setDrawOrder(new CombinedChart.DrawOrder[]{
            CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
        });
        decorLineChart(mChart);

        CombinedData data = new CombinedData();

        data.setData( lineData);
        //data.setData(barData);
        mChart.getDescription().setEnabled(false);
        mChart.setData(data);
        mChart.invalidate();
    }

    public void decorLineChart(CombinedChart mchart){

        mchart.animateXY(1000, 1000);
        mchart.setDrawGridBackground(false);
        XAxis xAxis = mchart.getXAxis();

        YAxis yAxis = mchart.getAxisLeft();

        setupAxis(xAxis, yAxis);

        Legend legend = mchart.getLegend();
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

    public void setupDatasetStyle(LineDataSet setCompA, LineDataSet setCompB) {//
        //Line
        setCompA.setDrawCircleHole(true);
        setCompA.setValueTextSize(12);
        setCompA.setValueTextColor(Color.GRAY);
        setCompA.setCircleHoleRadius(3);
        setCompA.setAxisDependency(YAxis.AxisDependency.LEFT);
        setCompA.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        setCompA.setAxisDependency(YAxis.AxisDependency.LEFT);
        setCompA.setColor(Color.rgb(229, 13, 92));
        //Bar
        setCompB.setColors(Color.rgb(47, 237, 208));
        setCompB.setValueTextColor(Color.rgb(60, 220, 78));
        setCompB.setValueTextSize(10f);
        setCompB.setAxisDependency(YAxis.AxisDependency.LEFT);

    }

    public void setupLegend(Legend legend) {
/*        legend.setEnabled(true);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setFormSize(7f); // set the size of the legend forms/shapes
        legend.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);

        legend.setTextSize(14f);
        legend.setTextColor(Color.GRAY);
        legend.setXEntrySpace(55); // set the space between the legend entries on the x-axis
        legend.setYEntrySpace(5f); // set the space between the legend entries on the y-axis
        // set custom labels and colors
        */
        legend.setWordWrapEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
    }

    public void setupAxis(XAxis xAxis, YAxis yAxis) {
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setAxisMinimum(0f);
        xAxis.setValueFormatter(formatter);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        yAxis.setTextSize(12f);
        yAxis.setTextColor(Color.BLACK);

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
