package com.hixel.hixel.comparisonGraph;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
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
import com.hixel.hixel.models.FinancialData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class GraphFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LineChart lineChart;
    //final String[] years = new String[] { };
    String[] years;

    private OnFragmentInteractionListener mListener;

    public GraphFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Graph.
     */
    // TODO: Rename and change types and number of parameters
    public static GraphFragment newInstance(String param1, String param2) {
        GraphFragment fragment = new GraphFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        lineChart = view.findViewById(R.id.chart1);
        return view;
    }

    public void drawGraph(GraphContract.Presenter mPresenter, String selectedRatio) {
        List<Entry> CompA = new ArrayList<>();
        List<Entry> CompB = new ArrayList<>();
        // Entry CompAYear1= new Entry(0f, (float) mPresenter.getCompanies().get(0).getFinancialDataEntries().get(0).getRatios()
        List<FinancialData> financialDataCompA =
                mPresenter.getCompanies().get(0).getFinancialDataEntries();
        List<FinancialData> financialDataCompB =
                mPresenter.getCompanies().get(1).getFinancialDataEntries();

        checkYearNull(financialDataCompA);
        checkYearNull(financialDataCompB);
        mPresenter.checkUpFinancialEntry(mPresenter.getCompanies().get(0));
        mPresenter.checkUpFinancialEntry(mPresenter.getCompanies().get(1));
        createListOfYears(financialDataCompA);
        //add company A data for graph
        LinkedHashMap<String, Double> DataCompAYear1 = financialDataCompA.get(4).getRatios();
        Entry CompAYear1 = new Entry(0f, Float.valueOf(DataCompAYear1.get(selectedRatio).toString()));
        CompA.add(CompAYear1);
        LinkedHashMap<String, Double> DataCompAYear2 = financialDataCompA.get(3).getRatios();
        Entry CompAYear2 = new Entry(1f, Float.valueOf(DataCompAYear2.get(selectedRatio).toString()));
        CompA.add(CompAYear2);
        LinkedHashMap<String, Double> DataCompAYear3 = financialDataCompA.get(2).getRatios();
        Entry CompAYear3 = new Entry(2f, Float.valueOf(DataCompAYear3.get(selectedRatio).toString()));
        CompA.add(CompAYear3);
        LinkedHashMap<String, Double> DataCompAYear4 = financialDataCompA.get(1).getRatios();
        Entry CompAYear4 = new Entry(3f, Float.valueOf(DataCompAYear4.get(selectedRatio).toString()));
        CompA.add(CompAYear4);
        LinkedHashMap<String, Double> DataCompAYear5 = financialDataCompA.get(0).getRatios();
        Entry CompAYear5 = new Entry(4f, Float.valueOf(DataCompAYear5.get(selectedRatio).toString()));
        CompA.add(CompAYear5);
        //add company b data for graph
        LinkedHashMap<String, Double> DataCompBYear1 = financialDataCompB.get(4).getRatios();
        Entry CompBYear1 = new Entry(0f, Float.valueOf(DataCompBYear1.get(selectedRatio).toString()));
        CompB.add(CompBYear1);
        LinkedHashMap<String, Double> DataCompBYear2 = financialDataCompB.get(3).getRatios();
        Entry CompBYear2 = new Entry(1f, Float.valueOf(DataCompBYear2.get(selectedRatio).toString()));
        CompB.add(CompBYear2);
        LinkedHashMap<String, Double> DataCompBYear3 = financialDataCompB.get(2).getRatios();
        Entry CompBYear3 = new Entry(2f, Float.valueOf(DataCompBYear3.get(selectedRatio).toString()));
        CompB.add(CompBYear3);
        LinkedHashMap<String, Double> DataCompBYear4 = financialDataCompB.get(1).getRatios();
        Entry CompBYear4 = new Entry(3f, Float.valueOf(DataCompBYear4.get(selectedRatio).toString()));
        CompB.add(CompBYear4);
        LinkedHashMap<String, Double> DataCompBYear5 = financialDataCompB.get(0).getRatios();
        Entry CompBYear5 = new Entry(4f, Float.valueOf(DataCompBYear5.get(selectedRatio).toString()));
        CompB.add(CompBYear5);

        LineDataSet setCompA = new LineDataSet(CompA, mPresenter.getCompanies().get(0).getIdentifiers().getName());

        setCompA.setColors(ColorTemplate.LIBERTY_COLORS);
        setupDatasetStyle(setCompA);
        setCompA.enableDashedLine(10f, 10f, 10f);

        LineDataSet setCompB = new LineDataSet(CompB, mPresenter.getCompanies().get(1).getIdentifiers().getName());

        setCompB.setColors(ColorTemplate.COLORFUL_COLORS);
        setupDatasetStyle(setCompB);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setCompA);
        dataSets.add(setCompB);
        LineData data = new LineData(dataSets);
        lineChart.animateXY(1000, 1000);
        lineChart.setData(data);
        lineChart.invalidate();
        Log.d("GET TO DRAW GRAPH", "88888888888");
        XAxis xAxis = lineChart.getXAxis();

        YAxis yAxis = lineChart.getAxisLeft();
        YAxis yRight = lineChart.getAxisRight();
        yRight.setTextColor(getResources().getColor(R.color.textColorDefault));
        setupAxis(xAxis, yAxis);

        Legend legend
                = lineChart.getLegend();
        setupLegend(legend);

    }

    IAxisValueFormatter formatter = new IAxisValueFormatter() {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return years[(int) value];
        }

        // we don't draw numbers, so no decimal digits needed

        public int getDecimalDigits() {
            return 0;
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

        for (int i= financialDataCompA.size()-1;i>=0;i--) {
            toConvertYears.add(String.valueOf(financialDataCompA.get(i).getYear()));

        }
        years = toConvertYears.toArray(new String[toConvertYears.size()]);
    }

    public void setupDatasetStyle(LineDataSet setCompA) {
        setCompA.setDrawCircleHole(true);
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
        //yAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        //yAxis.setValueFormatter(formatter);
        yAxis.setTextSize(12f);
        yAxis.setTextColor(Color.WHITE);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
