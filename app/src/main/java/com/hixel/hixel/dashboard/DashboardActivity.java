package com.hixel.hixel.dashboard;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.hixel.hixel.R;
import com.hixel.hixel.comparison.ComparisonActivity;
import com.hixel.hixel.databinding.ActivityDashboardBinding;

public class DashboardActivity extends AppCompatActivity implements DashboardContract.View,
        OnItemSelectedListener {

    private DashboardContract.Presenter mPresenter;
    RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityDashboardBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        setSupportActionBar(binding.toolbar.toolbar);
        binding.toolbar.toolbarTitle.setText(R.string.dashboard);

        // Init presenter
        mPresenter = new DashboardPresenter(this);
        mPresenter.start();

        Spinner spinner = binding.spinner;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.company_dropdown, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        RecyclerView mRecyclerView = binding.recyclerView;
        mAdapter = new DashboardAdapter(this, mPresenter);
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        final Intent moveToCompare = new Intent(this,ComparisonActivity.class);
        BottomNavigationView bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottom_nav); //bottom menu nad set onClick
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_button:



                    case R.id.compare_button:
                        startActivity(moveToCompare);


                    case R.id.settings_button:

                }
                return true;
            }
        });

    }

    @Override
    public void setPresenter(@NonNull DashboardContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        mPresenter.sortCompaniesBy(item);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void portfolioChanged() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*@Override
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
    }*/
}
