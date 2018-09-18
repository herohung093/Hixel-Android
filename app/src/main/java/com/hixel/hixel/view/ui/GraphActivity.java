package com.hixel.hixel.view.ui;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.hixel.hixel.R;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.viewmodel.GraphViewModel;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class GraphActivity extends FragmentActivity implements
        AdapterView.OnItemSelectedListener, GenericChartFragment.OnFragmentInteractionListener, GraphFragment.OnFragmentInteractionListener {
    private final String TAG = getClass().getSimpleName();

    ArrayList<String> ratios = new ArrayList<>();
    ArrayAdapter<String> listRatiosAdapter;
    Intent intentReceiver;
    Spinner listOfGraph;
    TextView companyA, companyB;
    ArrayList<Company> receivedCompanies;
    GraphViewModel graphViewModel;
    ImageView inforButton;


   // ActivityGraphBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        intentReceiver = getIntent();
        receivedCompanies =
            (ArrayList<Company>) intentReceiver.getSerializableExtra("COMPARISON_COMPANIES");

        Context context = this;
        //setup bottom navigator
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.graph_generic_navigator);
        setupBottomNavigationView(bottomNavigationView);
        Log.d(TAG,"COMPANIES SIZE" + receivedCompanies.size());

        graphViewModel= ViewModelProviders.of(this).get(GraphViewModel.class);
        //Observe changes in list of ratios
        observeViewModel(graphViewModel);

        companyA = findViewById(R.id.companyA);
        companyA.setText(receivedCompanies.get(0).getIdentifiers().getName());
        companyB = findViewById(R.id.companyB);
        companyB.setText(receivedCompanies.get(1).getIdentifiers().getName());

        inforButton = findViewById(R.id.imageView3);
        inforButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                final Dialog dialog= new Dialog(context);
                dialog.setContentView(R.layout.information_popup_window);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setTitle("Title...");
                int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
                int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
                dialog.getWindow().setLayout(width,height);
                ImageView close_ib =dialog.findViewById(R.id.popup_ib_close);;
                close_ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

    }

    public void setupListOfRatios(ArrayList<String> spinnerList) {
        listRatiosAdapter =  new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);
        listRatiosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listOfGraph = findViewById(R.id.spinner2);
        listOfGraph.setOnItemSelectedListener(this);
        listOfGraph.setAdapter(listRatiosAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        GraphFragment fragmentA =
                (GraphFragment) getFragmentManager().findFragmentById(R.id.fragment_bar_char);
        fragmentA.drawGraph(receivedCompanies, adapterView.getSelectedItem().toString());
        GenericChartFragment fragmentB =
            (GenericChartFragment) getFragmentManager().findFragmentById(R.id.fragment_radar_chart);
        fragmentB.drawGraph(receivedCompanies);
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
                    Intent moveToCompare = new Intent(this, ComparisonActivity.class);
                    startActivity(moveToCompare);
                    break;

                case R.id.settings_button:
                    Intent moveToProfile = new Intent(this,ProfileActivity.class);
                    startActivity(moveToProfile);
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
