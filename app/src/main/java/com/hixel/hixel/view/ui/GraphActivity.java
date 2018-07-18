package com.hixel.hixel.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.hixel.hixel.R;
import com.hixel.hixel.view.ui.GraphFragment.OnFragmentInteractionListener;
import com.hixel.hixel.service.models.Company;
import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends FragmentActivity implements
        AdapterView.OnItemSelectedListener, OnFragmentInteractionListener {

    List<String> ratios = new ArrayList<>();
    ArrayAdapter<String> listRatiosAdapter;
    Intent intentReceiver;
    Spinner listOfGraph;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        intentReceiver = getIntent();
        ArrayList<Company> receivedCompanies =
                (ArrayList<Company>) intentReceiver.getSerializableExtra("COMPARISON_COMPANIES");


        //setup bottom navigator
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_graph);
        setupBottomNavigationView(bottomNavigationView);

    }


    public void updateRatios(ArrayList<String> ratios1) {
        this.ratios=ratios1;
        listRatiosAdapter =  new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ratios);
        listRatiosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listOfGraph = findViewById(R.id.spinner);
        listOfGraph.setOnItemSelectedListener(this);
        listOfGraph.setAdapter(listRatiosAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        GraphFragment FragmentA =
                (GraphFragment) getFragmentManager().findFragmentById(R.id.graphFragment);
        // FragmentA.drawGraph(mPresenter.getCompanies(), adapterView.getSelectedItem().toString());
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
}
