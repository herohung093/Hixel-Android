package com.hixel.hixel.ui.commonui;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.hixel.hixel.R;
import com.hixel.hixel.data.entities.company.Company;
import java.util.ArrayList;
import java.util.List;


/**
 * Displays a line chart of the historical performance of the companies
 */
public class GraphFragment extends Fragment {

    private CombinedChart chart;
    private String[] years = {"2014", "2015", "2016", "2017", "2018"};
    ArrayList<Integer> colors = new ArrayList<>();

    public GraphFragment() { }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        chart =  view.findViewById(R.id.chart1);

        return view;
    }

    public void drawGraph(Company company, String selectedRatio) {
        LineData lineData = new LineData();
        ArrayList<LineDataSet> lineDataSets = new ArrayList<>();

        LineDataSet setComp = lineChartDataSetup(selectedRatio, company);
        lineDataSets.add(setComp);

        setupDatasetStyle(lineDataSets);

        for(int i = 0; i < lineDataSets.size();i++){
            lineData.addDataSet(lineDataSets.get(i));
        }

        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
        });

        decorLineChart(chart);

        CombinedData data = new CombinedData();
        data.setData(lineData);

        chart.getDescription().setEnabled(false);
        chart.setData(data);
        chart.invalidate();
    }

    public void drawGraph(List<Company> companies, String selectedRatio) {
        LineData lineData = new LineData();
        ArrayList<LineDataSet> lineDataSets = new ArrayList<>();

        for(int i = 0; i < companies.size(); i++){
            LineDataSet setComp = lineChartDataSetup(selectedRatio, companies.get(i));
            lineDataSets.add(setComp);
        }

        setupDatasetStyle(lineDataSets);

        for(int i = 0; i < lineDataSets.size();i++){
            lineData.addDataSet(lineDataSets.get(i));
        }

        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
        });

        decorLineChart(chart);

        CombinedData data = new CombinedData();
        data.setData(lineData);

        chart.getDescription().setEnabled(false);
        chart.setData(data);
        chart.invalidate();
    }

    /**
     * Sets up the LineDataSet for each company being compared.
     *
     * @param selectedRatio The ratio to be displayed
     * @param company The company to display.
     * @return The LineDataSet of the company.
     */
    public LineDataSet lineChartDataSetup(String selectedRatio, Company company){
        List<Entry> compEntry = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            compEntry.add(getCompanyEntry(i, selectedRatio, company));
        }

        return new LineDataSet(compEntry, company.getIdentifiers().getTicker());
    }

    private Entry getCompanyEntry(int index, String selectedRatio, Company company) {
        Entry entry;

        switch (selectedRatio) {
            case "Returns":
               entry = new Entry(index, (float) company.getDataEntries().get(index).getReturns());
                break;
            case "Performance":
                entry = new Entry(index, (float) company.getDataEntries().get(index).getPerformance());
                break;
            case "Strength":
               entry = new Entry(index, (float) company.getDataEntries().get(index).getStrength());
                break;
            case "Health":
                entry = new Entry(index, (float) company.getDataEntries().get(index).getHealth());
                break;
            case "Safety":
                entry = new Entry(index, (float) company.getDataEntries().get(index).getSafety());
                break;
            default:
                entry = new Entry(index, (float) company.getDataEntries().get(index).getRatios().debtToEquityRatio);
        }

        return entry;
    }

    /**
     * Styling for the line chart.
     * @param chart The line chart.
     */
    public void decorLineChart(CombinedChart chart){
        chart.animateXY(1000, 1000);
        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter((value, axis) -> {
            if (value == 0) {
                return "N/A";
            } else {
                return String.valueOf(value);
            }

        });

        YAxis yAxis = chart.getAxisLeft();
        setupAxis(xAxis, yAxis);

        Legend legend = chart.getLegend();
        setupLegend(legend);
    }


    public void setupDatasetStyle(ArrayList<LineDataSet> lineDataSets) {
        for (int i = 0; i < lineDataSets.size(); i++){
            lineDataSets.get(i).setDrawCircleHole(true);
            lineDataSets.get(i).setValueTextSize(12);
            lineDataSets.get(i).setValueTextColor(colors.get(i));
            lineDataSets.get(i).setCircleHoleRadius(3);
            lineDataSets.get(i).setAxisDependency(YAxis.AxisDependency.LEFT);
            lineDataSets.get(i).setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSets.get(i).setAxisDependency(YAxis.AxisDependency.LEFT);
            lineDataSets.get(i).setColor(colors.get(i));
        }
    }

    public void setupLegend(Legend legend) {
        legend.setWordWrapEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
    }

    public void setupAxis(XAxis xAxis, YAxis yAxis) {
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setAxisMinimum(0.0f);

        IAxisValueFormatter formatter = (value, axis) -> years[(int) value];
        xAxis.setValueFormatter(formatter);

        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        yAxis.setTextSize(12f);
        yAxis.setTextColor(Color.BLACK);

        yAxis.setAxisMinimum(0.0f);
        yAxis.setAxisMaximum(5.0f);
        yAxis.setGranularity(1f);

        chart.getAxisRight().setAxisMinimum(0.0f);
        chart.getAxisRight().setAxisMaximum(5.0f);
        chart.getAxisRight().setGranularity(1f);
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
