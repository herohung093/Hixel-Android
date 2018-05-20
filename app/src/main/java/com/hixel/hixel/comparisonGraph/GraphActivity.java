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

public class GraphActivity extends FragmentActivity implements GraphContract.View,AdapterView.OnItemSelectedListener,GraphFagment.OnFragmentInteractionListener {
    ArrayList<String> Ratios=new ArrayList<String>();
    ArrayAdapter<String> listRatiosAdapter;
    Intent intentReceiver;
    Spinner listOfGraph;
    private GraphContract.Presenter mpresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        intentReceiver=getIntent();
        ArrayList<Company> receivedCompanies = (ArrayList< Company>) intentReceiver.getSerializableExtra("selectedCompanies");
        mpresenter= new GraphPresenter( this, receivedCompanies);
        mpresenter.start();
        Ratios.add("Current Ratio");
        Ratios.add("Quick Ratio");
        Ratios.add("Cash Ratio");
        Ratios.add("Debt-to-Equity Ratio");
        //Ratios.add("Health");
        Ratios.add("Long Term Debt-to-Equity Ratio");

        listRatiosAdapter=  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Ratios);
        listRatiosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listOfGraph=(Spinner) findViewById(R.id.spinner);
        listOfGraph.setOnItemSelectedListener(this);
        listOfGraph.setAdapter(listRatiosAdapter);


    }

    @Override
    public void graphChanged(String ratio) {

    }

    @Override
    public void setPresenter(GraphContract.Presenter presenter) {
        mpresenter=presenter;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        GraphFagment FragmentA= (GraphFagment) getFragmentManager().findFragmentById(R.id.graphFragment);
        FragmentA.drawGraph(mpresenter,adapterView.getSelectedItem().toString());
        Log.d("striggered draw graph","88888888888");

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
