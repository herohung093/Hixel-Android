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
import java.util.Random;

public class CompanyGenericGraphFragment extends Fragment implements GraphInterface {

    private CombinedChart mChart;
    String[] years={"2013","2014","2015","2016","2017",};
    ArrayList<Integer> colors =new ArrayList<>();
    private OnFragmentInteractionListener mListener;


    public CompanyGenericGraphFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        colors.add(Color.rgb(255,218,185));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_generic_graph, container, false);
        mChart =  view.findViewById(R.id.chart11);
        return view;
    }

    public LineDataSet lineChartDataSetup( String selectedRatio, Company company){
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
    public void drawGraph(Company company,String selectedRatio){

        LineData lineData = new LineData();
        ArrayList<LineDataSet> lineDataSets = new ArrayList<>();

        LineDataSet setComp= lineChartDataSetup( selectedRatio, company);

        lineDataSets.add(setComp);



        setupDatasetStyle(lineDataSets);
        for(int i=0;i<lineDataSets.size();i++){
            lineData.addDataSet(lineDataSets.get(i));
        }
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

    @Override
    public void drawGraph(List<Company> companies, String selectedRatio) {

    }

    public void decorLineChart(CombinedChart mchart){

        mchart.animateXY(1000, 1000);
        mchart.setDrawGridBackground(false);
        XAxis xAxis = mchart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(value ==0)
                    return "N/A";
                else return String.valueOf(value);
            }
        });
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

    private void checkYearNull(List<CompanyData> financial) {
        /*
        for (int i = 0; i < financial.size(); i++) {
            if (financial.get(i) == null) {
                financial.get(i).setDefaultFinancialData(); //set all values equal to -0 for visualising purpose
                financial.get(i).setYear(financial.get(i - 1).getYear() - 1);
            }
        }*/
    }

    private void createListOfYears(List<CompanyData> financialDataCompA) {
        /*
        List<String> toConvertYears = new ArrayList<>();

        for (int i = financialDataCompA.size() - 1; i >= 0; i--) {
            toConvertYears.add(String.valueOf(financialDataCompA.get(i).getYear()));
        }

        years = toConvertYears.toArray(new String[toConvertYears.size()]);
        */
    }

    public void setupDatasetStyle(ArrayList<LineDataSet> lineDataSets/*, LineDataSet setCompA, LineDataSet setCompB*/) {


        for (int i=0;i<lineDataSets.size();i++ ){
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}