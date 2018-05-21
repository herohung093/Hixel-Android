package com.hixel.hixel.comparisonGraph;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.hixel.hixel.R;
import com.hixel.hixel.models.Company;

import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends FragmentActivity implements GraphContract.View,
        AdapterView.OnItemSelectedListener, GraphFragment.OnFragmentInteractionListener {

    List<String> ratios = new ArrayList<>();
    ArrayAdapter<String> listRatiosAdapter;
    Intent intentReceiver;
    Spinner listOfGraph;
    private GraphContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        intentReceiver = getIntent();
        List<Company> receivedCompanies =
                (ArrayList<Company>) intentReceiver.getSerializableExtra("selectedCompanies");

        mPresenter = new GraphPresenter( this, receivedCompanies);
        mPresenter.start();

        ratios.add("Current Ratio");
        ratios.add("Quick Ratio");
        ratios.add("Cash Ratio");
        ratios.add("Debt-to-Equity Ratio");
        //ratios.add("Health");
        ratios.add("Long Term Debt-to-Equity Ratio");

        listRatiosAdapter =  new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ratios);
        listRatiosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        listOfGraph = findViewById(R.id.spinner);
        listOfGraph.setOnItemSelectedListener(this);
        listOfGraph.setAdapter(listRatiosAdapter);
    }

    @Override
    public void graphChanged(String ratio) {

    }

    @Override
    public void setPresenter(GraphContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateRatios(ArrayList<String> ratios1) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        GraphFragment FragmentA =
                (GraphFragment) getFragmentManager().findFragmentById(R.id.graphFragment);

        FragmentA.drawGraph(mPresenter, adapterView.getSelectedItem().toString());
        Log.d("triggered draw graph","88888888888");

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
