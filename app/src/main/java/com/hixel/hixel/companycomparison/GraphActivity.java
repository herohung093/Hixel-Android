package com.hixel.hixel.companycomparison;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.hixel.hixel.R;
import com.hixel.hixel.dashboard.DashboardActivity;
import com.hixel.hixel.databinding.ActivityGraphBinding;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.companycomparison.GraphFragment.OnFragmentInteractionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class GraphActivity extends FragmentActivity implements
        AdapterView.OnItemSelectedListener, OnFragmentInteractionListener {
    private final String TAG = getClass().getSimpleName();

    ArrayList<String> ratios = new ArrayList<>();
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

        receivedCompanies =
            (ArrayList<Company>) intentReceiver.getSerializableExtra("COMPARISON_COMPANIES");

        //setup bottom navigator
        BottomNavigationView bottomNavigationView = (BottomNavigationView)binding.bottomNavGraph;
        setupBottomNavigationView(bottomNavigationView);
        Log.d(TAG,"COMPANIES SIZE" + receivedCompanies.size());
        graphViewModel= ViewModelProviders.of(this).get(GraphViewModel.class);
        //Observe changes in list of ratios
        observeViewModel(graphViewModel);

    }

    public void setupListOfRatios(ArrayList<String> sprinnerList) {
        listRatiosAdapter =  new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sprinnerList);
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
        bottomNavigationView.setSelectedItemId(R.id.compare_button);
        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.home_button:
                    Intent moveToDashBoard = new Intent(this, DashboardActivity.class);
                    startActivity(moveToDashBoard);
                    break;

                case R.id.compare_button:
                    Intent moveToCompare = new Intent(this, CompanyComparisonActivity.class);
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
            for (int i = 0; i<c.getFinancialDataEntries().size(); i++) {
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
    private void observeViewModel(GraphViewModel graphViewModel){
        graphViewModel.getRatios().observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(@Nullable ArrayList<String> strings) {
                if (strings != null) {
                    ratios = strings;
                    setupListOfRatios(strings);
                    checkUpFinancialEntry(strings);
                }
            }
        });
    }
}
