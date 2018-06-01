package com.hixel.hixel.dashboard;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import android.widget.Toast;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.hixel.hixel.R;
import com.hixel.hixel.comparison.ComparisonActivity;
import com.hixel.hixel.databinding.ActivityDashboardBinding;

import com.hixel.hixel.search.SearchEntry;
import io.reactivex.subjects.PublishSubject;
import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements DashboardContract.View,
        OnItemSelectedListener {

    private static final String TAG = DashboardActivity.class.getSimpleName();

    private DashboardContract.Presenter presenter;
    RecyclerView.Adapter dashboardAdapter;
    ActivityDashboardBinding binding;
    RecyclerView mRecyclerView;
    private RadarChart chart;
    AutoCompleteTextView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        // Set up the toolbar
        setSupportActionBar(binding.toolbar.toolbar);
        binding.toolbar.toolbarTitle.setText(R.string.dashboard);

        // Set up the dropdown options
        Spinner spinner = binding.spinner;
        ArrayAdapter<CharSequence> dropdownAdapter = ArrayAdapter.createFromResource(
                this, R.array.company_dropdown, android.R.layout.simple_spinner_item);
        dropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dropdownAdapter);
        spinner.setOnItemSelectedListener(this);

        // Set up the list of companies
        mRecyclerView = binding.recyclerView;

        // Set up the bottom navigation bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) binding.bottomNav;
        setupBottomNavigationView(bottomNavigationView);

        // UI for the chart
        setupChart();

        // Search
        PublishSubject<String> subject = PublishSubject.create();
        search = binding.toolbar.search;

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                subject.onNext(s.toString());
            }
        });

        // Init presenter
        presenter = new DashboardPresenter(this);
        presenter.start();

        presenter.search(subject);
    }

    @Override
    public void showSuggestions(List<SearchEntry> searchEntries) {
        ArrayAdapter<SearchEntry> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchEntries);
        search.setAdapter(adapter);
    }

    // NOTE: Using this as UI debugging tool
    @Override
    public void toasty() {
        Toast.makeText(this, "WOWZA", Toast.LENGTH_LONG).show();
    }

    // TODO: Implement this properly
    @Override
    public void setPresenter(@NonNull DashboardContract.Presenter presenter) {
        // presenter = presenter;
    }

    // TODO: Implement this so the default is nothing selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        presenter.sortCompaniesBy(item);
        // dashboardAdapter.notifyDataSetChanged();
    }

    public void setupBottomNavigationView(BottomNavigationView bottomNavigationView) {

        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.home_button:
                    // Already on this screen.
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

    @Override
    public void setupChart() {
        chart = binding.chart;

        // Configuring the chart
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setWebColor(Color.WHITE);
        chart.setWebColorInner(Color.WHITE);
        chart.setWebLineWidth(1f);
        chart.animateY(1400);
        chart.setWebAlpha(100);
        chart.setTouchEnabled(false);

        // Scale the size of the chart
        chart.setScaleX(1.2f);
        chart.setScaleY(1.2f);

        // XAxis is the outer web
        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(9f);
        xAxis.setXOffset(0);
        xAxis.setYOffset(0);

        // Seems to be the only way to get Strings to be the XAxis labels
        // Note: Seems to be that the longest string sets the margins for all other strings
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            private String[] ratioNames = {
                    "ROE",
                    "Cash Ratio",
                    "Debt-to-Equity",
                    "Current Ratio",
                    "Quick Ratio",

            };

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return ratioNames[(int) value % ratioNames.length];
            }

        });

        // YAxis is the inner web
        YAxis yAxis = chart.getYAxis();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(2.0f);
        yAxis.setLabelCount(5);
        yAxis.setDrawLabels(false);
    }

    @Override
    public void populateChart() {
        List<RadarEntry> entries = new ArrayList<>();

        // TODO: get data from portfolio
        entries.add(new RadarEntry(1.25f));
        entries.add(new RadarEntry(1.5f));
        entries.add(new RadarEntry(2.0f));
        entries.add(new RadarEntry(0.75f));
        entries.add(new RadarEntry(1.0f));

        RadarDataSet dataSet = new RadarDataSet(entries, "");
        dataSet.setColor(Color.parseColor("#4BCA81"));
        dataSet.setFillColor(Color.parseColor("#4BCA81"));
        dataSet.setDrawFilled(true);

        RadarData data = new RadarData(dataSet);
        data.setDrawValues(false);

        chart.setData(data);
        chart.invalidate();
    }

    @Override
    public void setupDashboardAdapter() {
        dashboardAdapter = new DashboardAdapter(this, presenter);
        mRecyclerView.setAdapter(this.dashboardAdapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void updateRatios(ArrayList<String> ratios1) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void showLoadingIndicator(final boolean active) {
        final ProgressBar progressBar = binding.progressBar;
        progressBar.setVisibility(active ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showLoadingError() {
        Snackbar.make(binding.getRoot(), "Error loading your portfolio", Snackbar.LENGTH_LONG)
                .setAction("RETRY", view -> presenter.loadPortfolio())
                .show();
    }

}

