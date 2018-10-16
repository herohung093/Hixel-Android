package com.hixel.hixel.ui.companydetail;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
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
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.entities.CompanyData;
import com.hixel.hixel.ui.GraphInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Displays a line chart of the historical performance of the Companies ratios.
 */
public class CompanyGenericGraphFragment extends Fragment implements GraphInterface {

    private CombinedChart chart;
    String[] years={"2013","2014","2015","2016","2017"};
    ArrayList<Integer> colors = new ArrayList<>();
    // TODO: Remove this field, it is not being used.
    private OnFragmentInteractionListener mListener;

    public CompanyGenericGraphFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        colors.add(Color.rgb(255,218,185));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_generic_graph, container, false);
        chart =  view.findViewById(R.id.chart11);
        return view;
    }

    /**
     * Creates the LineDataSet for the chart using the ratio currently selected by the user and
     * the Company.
     *
     * @param selectedRatio The currently selected ratio.
     * @param company The company.
     * @return LineDataSet for the chart.
     */
    public LineDataSet lineChartDataSetup(String selectedRatio, Company company){
        List<Entry> compEntry = new ArrayList<>();
        /*List<FinancialData> financialData= company.getFinancialDataEntries();
        checkYearNull(financialData);
*/
        //createListOfYears(financialData);

        int j=4;
        for (int i=0;i<5;i++){
            //LinkedHashMap<String, Double> DataCompAYear1 = financialData.get(j).getRatios();
            j--;
            Random rand = new Random();
            Entry compYearData = new Entry(i, Float.valueOf(rand.nextInt(5)+1));
            compEntry.add(compYearData);
        }

        return new LineDataSet(compEntry,company.getTicker());
    }

    @Override
    public void drawGraph(Company company, String selectedRatio){
        LineData lineData = new LineData();
        ArrayList<LineDataSet> lineDataSets = new ArrayList<>();

        LineDataSet setComp = lineChartDataSetup( selectedRatio, company);
        lineDataSets.add(setComp);

        setupDatasetStyle(lineDataSets);
        for(int i=0;i<lineDataSets.size();i++){
            lineData.addDataSet(lineDataSets.get(i));
        }

        //draw graph
        chart.setDrawOrder(new CombinedChart.DrawOrder[] {
                CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
        });

        decorLineChart(chart);

        CombinedData data = new CombinedData();

        data.setData(lineData);
        // TODO: This is not being used, can it be removed?
        //data.setData(barData);
        chart.getDescription().setEnabled(false);
        chart.setData(data);
        chart.invalidate();

    }

    @Override
    public void drawGraph(List<Company> companies, String selectedRatio) { }

    /**
     * Styles the line chart.
     * @param chart The companies chart.
     */
    public void decorLineChart(CombinedChart chart){
        chart.animateXY(1000, 1000);
        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        // TODO: Change to ternary
        xAxis.setValueFormatter((value, axis) -> {
            if(value == 0)
                return "N/A";
            else {
                return String.valueOf(value);
            }
        });

        YAxis yAxis = chart.getAxisLeft();
        setupAxis(xAxis, yAxis);

        Legend legend = chart.getLegend();
        setupLegend(legend);
    }


    /**
     * Sets an null data to the amount of the previous year
     * @param financial The companies performance data.
     */
    // TODO: Better documentation.
    private void checkYearNull(List<CompanyData> financial) {
        /*
        for (int i = 0; i < financial.size(); i++) {
            if (financial.get(i) == null) {
                financial.get(i).setDefaultFinancialData(); //set all values equal to -0 for visualising purpose
                financial.get(i).setYear(financial.get(i - 1).getYear() - 1);
            }
        }*/
    }

    /**
     * Converts the CompanyData year (int) into a String to be read by the formatter.
     * @param financialDataCompA The companies financial data.
     */
    private void createListOfYears(List<CompanyData> financialDataCompA) {
        /*
        List<String> toConvertYears = new ArrayList<>();

        for (int i = financialDataCompA.size() - 1; i >= 0; i--) {
            toConvertYears.add(String.valueOf(financialDataCompA.get(i).getYear()));
        }

        years = toConvertYears.toArray(new String[toConvertYears.size()]);
        */
    }

    /**
     * Styles the line of the chart.
     * @param lineDataSets The lines for the chart.
     */
    public void setupDatasetStyle(ArrayList<LineDataSet> lineDataSets) {
        for (int i = 0; i < lineDataSets.size(); i++ ){
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

    /**
     * Styles the legend of the chart.
     * @param legend The legend of the chart.
     */
    public void setupLegend(Legend legend) {
        legend.setWordWrapEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
    }

    /**
     * Styles the axis of the chart.
     * @param xAxis X-axis of the chart.
     * @param yAxis Y-axis of the chart.
     */
    public void setupAxis(XAxis xAxis, YAxis yAxis) {
        IAxisValueFormatter formatter = (value, axis) -> years[(int) value];

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

    public interface OnFragmentInteractionListener {
        // TODO: Not being used.
        void onFragmentInteraction(Uri uri);
    }
}