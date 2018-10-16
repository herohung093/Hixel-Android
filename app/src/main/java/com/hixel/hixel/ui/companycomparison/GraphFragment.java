package com.hixel.hixel.ui.companycomparison;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
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


/**
 * Displays a line chart of the historical performance of the companies
 */
// TODO: This class is extremely similar the the one in the companydetail, can they be merged?
public class GraphFragment extends Fragment implements GraphInterface {
    @SuppressWarnings("unused")
    private static final String TAG = GraphFragment.class.getSimpleName();

    private List<Company> companies;

    private CombinedChart chart;
    String[] years;
    ArrayList<Integer> colors = new ArrayList<>();

    // TODO: Not used.
    private OnFragmentInteractionListener listener;

    public GraphFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        colors.add(Color.rgb(255,218,185));
        colors.add(Color.rgb(139,136,120));
        colors.add(Color.rgb(208,32,144));
        colors.add(Color.rgb(	193,205,193));
        colors.add(Color.rgb(230,230,250));
        colors.add(Color.rgb(	100,149,237));
        colors.add(Color.rgb(	106,90,205));
        colors.add(Color.rgb(0,255,127));
        colors.add(Color.rgb(255,215,0));
        colors.add(Color.rgb(	205,92,92));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        chart =  view.findViewById(R.id.chart1);

        return view;
    }

    @Override
    public void drawGraph(List<Company> companies, String selectedRatio) {
        this.companies = companies;

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
    // TODO: Make this display 5 years of data
    public LineDataSet lineChartDataSetup(String selectedRatio, Company company){
        List<Entry> compEntry = new ArrayList<>();

        // checkYearNull(companyData);
        createListOfYears();

        for (int i = 0; i < 5; i++) {
            compEntry.add(getCompanyEntry(i, selectedRatio, company));
        }

        return new LineDataSet(compEntry, company.getTicker());
    }

    private Entry getCompanyEntry(int index, String selectedRatio, Company company) {
        Entry entry;

        switch (selectedRatio) {
            case "Returns":
                entry = new Entry(index, (float) company.getReturnsScore());
                break;
            case "Performance":
                entry = new Entry(index, (float) company.getPerformanceScore());
                break;
            case "Strength":
                entry = new Entry(index, (float) company.getStrengthScore());
                break;
            case "Health":
                entry = new Entry(index, (float) company.getHealthScore());
                break;
            case "Safety":
                entry = new Entry(index, (float) company.getSafetyScore());
                break;
            default:
                entry = new Entry(index, (float) company.getCurrentRatio());
        }

        return entry;
    }

    public void updateChart(String selectedRatio) {

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

        CombinedData data = new CombinedData();
        data.setData(lineData);

        chart.setData(data);
        chart.invalidate();
    }

    @Override
    public void drawGraph(Company company, String selectedRatio) {
        // TODO: Why is this here?
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
            if(value == 0) {
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


    IAxisValueFormatter formatter = new IAxisValueFormatter() {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return years[(int) value];
        }
    };

    private void checkYearNull(List<CompanyData> financial) {
        for (int i = 0; i < financial.size(); i++) {
            if (financial.get(i) == null) {
               // financial.get(i).setDefaultFinancialData(); //set all values equal to -0 for visualising purpose
               // financial.get(i).setYear(financial.get(i - 1).getYear() - 1);
            }
        }
    }

    private void createListOfYears() {
        String[] y = {"1", "2", "3", "4", "5"};

        years = y;// toConvertYears.toArray(new String[toConvertYears.size()]);
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
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        // void onFragmentInteraction(Uri uri);
    }
}
