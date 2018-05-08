package com.hixel.hixel.dashboard;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.hixel.hixel.MainGraphDataSet;
import com.hixel.hixel.R;
import com.hixel.hixel.data.Company;
import com.hixel.hixel.databinding.ActivityDashboardBinding;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements DashboardContract.View,
        OnItemSelectedListener {

    private DashboardContract.Presenter mPresenter;
    private ActivityDashboardBinding binding;
    RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        // Setup Toolbar
        setSupportActionBar(binding.toolbar.toolbar);
        binding.toolbar.toolbarTitle.setText(R.string.dashboard);

        mPresenter = new DashboardPresenter(this);
        mPresenter.start();

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.company_dropdown, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Set up list of companies
        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        mAdapter = new DashboardRecyclerViewAdapter(this, mPresenter);

        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void setPresenter(@NonNull DashboardContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMainGraph(ArrayList<Company> companies) {
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < companies.size(); i++) {
            entries.add(new BarEntry((float) i, (float) companies.get(i).getHealth()));
        }

        BarChart chart = binding.chart;

        MainGraphDataSet set = new MainGraphDataSet(entries, "Health");
        set.setColors(ContextCompat.getColor(this, R.color.good),
                ContextCompat.getColor(this, R.color.average),
                ContextCompat.getColor(this, R.color.bad));
        set.setValueTextColor(R.color.textColorDefault);
        BarData barData = new BarData(set);

        chart.getAxisLeft().setDrawLabels(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getXAxis().setDrawLabels(false);

        chart.getLegend().setEnabled(false);   // Hide the legend

        chart.setData(barData);
        chart.animateY(1000);
        chart.animateX(1000);
        chart.invalidate(); // refresh
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        mPresenter.sortCompanies(item);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
