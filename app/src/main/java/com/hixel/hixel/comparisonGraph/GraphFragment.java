package com.hixel.hixel.comparisonGraph;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hixel.hixel.R;
import com.hixel.hixel.models.Company;
import com.hixel.hixel.models.FinancialData;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class GraphFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    // private static final String ARG_PARAM1 = "param1";
    // private static final String ARG_PARAM2 = "param2";

    LineChart lineChart;
    String[] years;

    private OnFragmentInteractionListener mListener;

    public GraphFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    /*
    public static GraphFragment newInstance(String param1, String param2) {
        GraphFragment fragment = new GraphFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }*/

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
        checkYearNull(financialData);

        createListOfYears(financialData);
        int j=4;
        for (int i=0;i<5;i++){
            LinkedHashMap<String, Double> DataCompAYear1 = financialData.get(j).getRatios();
            j--;
            Entry compYearData = new Entry(i, Float.valueOf(DataCompAYear1.get(selectedRatio).toString()));
            compEntry.add(compYearData);
        }

        return new LineDataSet(compEntry,company.getIdentifiers().getName());
    }
    public void drawGraph(ArrayList<Company> companies,String selectedRatio){
        List<ILineDataSet> dataSets = new ArrayList<>();

        for(Company c: companies){
            LineDataSet setCompA= lineChartDataSetup( selectedRatio, c);
            if(companies.size()==1){
                setCompA.setColors(ColorTemplate.LIBERTY_COLORS);
                setupDatasetStyle(setCompA);
                setCompA.enableDashedLine(10f, 10f, 10f);
                dataSets.add(setCompA);
            }else if(companies.size()==2 && companies.indexOf(c)==0){
                setCompA.setColors(ColorTemplate.LIBERTY_COLORS);
                setupDatasetStyle(setCompA);
                setCompA.enableDashedLine(10f, 10f, 10f);
                dataSets.add(setCompA);
            } else {
                setCompA.setColors(ColorTemplate.COLORFUL_COLORS);
                setupDatasetStyle(setCompA);
                dataSets.add(setCompA);
            }

        }

        LineData data = new LineData(dataSets);
        decorLineChart(lineChart,data);
    }
    public void decorLineChart(LineChart lineChart, LineData data){

        lineChart.animateXY(1000, 1000);
        lineChart.setData(data);
        lineChart.invalidate();
        Log.d("GOT TO DRAW GRAPH", "Successful");
        XAxis xAxis = lineChart.getXAxis();

        YAxis yAxis = lineChart.getAxisLeft();
        YAxis yRight = lineChart.getAxisRight();
        yRight.setTextColor(R.color.textColorDefault);
        setupAxis(xAxis, yAxis);

        Legend legend = lineChart.getLegend();
        setupLegend(legend);
    }


    IAxisValueFormatter formatter = new IAxisValueFormatter() {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return years[(int) value];
        }

        // we don't draw numbers, so no decimal digits needed
        /*
        public int getDecimalDigits() {
            return 0;
        }*/
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

    public void setupDatasetStyle(LineDataSet setCompA) {
        setCompA.setDrawCircleHole(true);
        // TODO: Check if deprecated method is required.
        setCompA.setCircleSize(7);
        setCompA.setValueTextSize(12);
        setCompA.setValueTextColor(Color.WHITE);
        setCompA.setCircleHoleRadius(3);
        setCompA.setAxisDependency(YAxis.AxisDependency.LEFT);

    }

    public void setupLegend(Legend legend) {
        legend.setEnabled(true);
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


    // TODO: Rename method, update argument and hook method into UI event
    /*
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

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
