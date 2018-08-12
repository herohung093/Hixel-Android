package com.hixel.hixel.view.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.hixel.hixel.R;
import com.hixel.hixel.databinding.ActivityGraphBinding;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.view.ui.GraphFragment.OnFragmentInteractionListener;
import com.hixel.hixel.viewmodel.GraphViewModel;
import java.util.ArrayList;
import java.util.LinkedHashMap;


public class GraphActivity extends FragmentActivity implements
        AdapterView.OnItemSelectedListener, OnFragmentInteractionListener {
    private final String TAG = getClass().getSimpleName();

    ArrayList<String> ratios =new ArrayList<String>();
    ArrayAdapter<String> listRatiosAdapter;
    Intent intentReceiver;
    Spinner listOfGraph;
    ArrayList<Company> receivedCompanies;
    GraphViewModel graphViewModel;
    ActivityGraphBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_graph);
        intentReceiver = getIntent();
        //TODO: get ratios by doMeta();
        ratios.add("Current Ratio");
        ratios.add("Debt-to-Equity Ratio");
        ratios.add("Return-on-Equity Ratio");
        ratios.add("Return-on-Assets Ratio");
        ratios.add("Profit-Margin Ratio");

        receivedCompanies =
            (ArrayList<Company>) intentReceiver.getSerializableExtra("COMPARISON_COMPANIES");
        //setup bottom navigator
        BottomNavigationView bottomNavigationView = (BottomNavigationView)binding.bottomNavGraph;
        setupBottomNavigationView(bottomNavigationView);
        Log.d(TAG,"COMPANIES SIZE" + receivedCompanies.size());
        graphViewModel= ViewModelProviders.of(this).get(GraphViewModel.class);
        graphViewModel.loadCompanies(receivedCompanies);

        Log.d(TAG,"temporary list: "+ ratios.toString());
        setupListOfRatios();
        checkUpFinancialEntry(ratios);

    }


    public void setupListOfRatios() {

        listRatiosAdapter =  new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ratios);
        Log.d(TAG, "Ratios Size "+String.valueOf(ratios.size()));
        listRatiosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listOfGraph = binding.spinner;
        listOfGraph.setOnItemSelectedListener(this);
        listOfGraph.setAdapter(listRatiosAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        GraphFragment FragmentA =
                (GraphFragment) getFragmentManager().findFragmentById(R.id.graphFragment);
         FragmentA.drawGraph(receivedCompanies, adapterView.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void setupBottomNavigationView(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.home_button:
                    Intent moveToDashBoard = new Intent(this, DashboardActivity.class);
                    startActivity(moveToDashBoard);

                    break;
                case R.id.compare_button:
                    Intent moveToCompare = new Intent(this, ComparisonActivity.class);
                    startActivity(moveToCompare);
                    break;
                case R.id.settings_button:
                    // This screen is yet to be implemented
                    break;
            }

            return true;
        });
    }

    public void checkUpFinancialEntry(ArrayList<String> toBeCheckRatios) {
        for (Company c : receivedCompanies) {
            for (int i=0;i<c.getFinancialDataEntries().size();i++) {
                LinkedHashMap<String, Double> ratiosData = c.getFinancialDataEntries().get(i).getRatios();

                for (String k : toBeCheckRatios) {
                    if (ratiosData.get(k) == null) {
                        Log.d(String.valueOf(c.getFinancialDataEntries().get(i).getYear()) + k + ": ", "NULL***");
                        c.getFinancialDataEntries().get(i).getRatios().put(k,0.0);
                    }
                }
            }
        }
    }
}
