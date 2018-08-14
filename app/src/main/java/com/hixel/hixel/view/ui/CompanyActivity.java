package com.hixel.hixel.view.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.hixel.hixel.R;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.view.adapter.SearchAdapter;
import com.hixel.hixel.service.models.SearchEntry;
import com.hixel.hixel.viewmodel.CompanyViewModel;
import java.util.ArrayList;
import java.util.List;

public class CompanyActivity extends AppCompatActivity {

    CompanyViewModel companyViewModel;

    SearchView search;
    SearchView.SearchAutoComplete searchAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        companyViewModel = ViewModelProviders.of(this).get(CompanyViewModel.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        setupBottomNavigationView();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        Company company = (Company) extras.getSerializable("CURRENT_COMPANY");

        ArrayList<Company> companies = (ArrayList<Company>) extras.getSerializable("PORTFOLIO");

        Log.d("COMPANY_ACTIVITY", "" + company.getIdentifiers().getName());

        // Set up the toolbar
        setSupportActionBar(findViewById(R.id.toolbar));

        // presenter = new CompanyPresenter(this);

        // presenter.setCompany((Company) extras.getSerializable("CURRENT_COMPANY"));

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        // toolbarTitle.setText(presenter.getCompanyName());

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(v -> {
            Intent backIntent = getIntent();
            backIntent.putExtra("COMPANY_ADD", company);
            setResult(RESULT_OK,backIntent);
            finish();
        });

        if (companies != null) {
            for (Company c : companies) {
                //if (c.getIdentifiers().getName().equals(presenter.getCompanyName())) {
                //    fab.setVisibility(View.INVISIBLE);
                // }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem searchView = menu.findItem(R.id.action_search);

        search = (SearchView) searchView.getActionView();
        searchAutoComplete = search.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        // Styling the search bar
        searchAutoComplete.setHintTextColor(Color.WHITE);
        searchAutoComplete.setTextColor(Color.WHITE);
        ImageView searchClose = search.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchClose.setImageResource(R.drawable.ic_clear);


        searchAutoComplete.setOnItemClickListener((adapterView, view, itemIndex, id) -> {
            SearchEntry entry = (SearchEntry)adapterView.getItemAtPosition(itemIndex);
            String ticker = entry.getTicker();
            // presenter.loadDataForAParticularCompany(ticker);

            // presenter.setTickerFromSearchSuggestion(ticker);
            // call the load to portfolio method from here
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // presenter.loadSearchResults(searchAutoComplete.getText().toString());
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    public void showSearchResults(List<SearchEntry> searchEntries) {
        SearchAdapter adapter = new SearchAdapter(this, searchEntries);

        searchAutoComplete.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void updateRatios(ArrayList<String> ratios1) {
        TextView liquidityScore = findViewById(R.id.liquidity_score);
        TextView leverageScore = findViewById(R.id.leverage_score);
        TextView healthScore = findViewById(R.id.health_score);
        liquidityScore.setText(getValue(ratios1.get(0), 2017));
        leverageScore.setText(getValue(ratios1.get(1), 2017));
        healthScore.setText(getValue(ratios1.get(2), 2017));

        RadarChart chart = findViewById(R.id.chart);

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
        yAxis.setAxisMaximum(1.0f);
        yAxis.setLabelCount(5);
        yAxis.setDrawLabels(false);


        List<RadarEntry> entries = new ArrayList<>();

        // TODO: get data from portfolio
        // Currently generating random number between 0 and 1
        // this will hopefully look like its attached to the server
        for (int i = 0; i < 5; i++) {
            entries.add(new RadarEntry((float) Math.random()));
        }

        RadarDataSet dataSet = new RadarDataSet(entries, "");
        dataSet.setColor(Color.parseColor("#4BCA81"));
        dataSet.setFillColor(Color.parseColor("#4BCA81"));
        dataSet.setDrawFilled(true);

        RadarData data = new RadarData(dataSet);
        data.setDrawValues(false);

        chart.setData(data);
        chart.invalidate();

        // call the method to setup the values
        ArrayList<String> ratiosList = new ArrayList<>();
        ratiosList.add(getValue(ratios1.get(0), 2017));
        ratiosList.add(getValue(ratios1.get(1), 2017));
        ratiosList.add(getValue(ratios1.get(2), 2017));
        ratiosList.add(getValue(ratios1.get(3), 2017));
        ratiosList.add(getValue(ratios1.get(4), 2017));

        TextView tv4 = findViewById(R.id.textView4);
        TextView tv5 = findViewById(R.id.textView5);
        TextView tv6 = findViewById(R.id.textView6);

        tv4.setText(R.string.current_ratio);
        tv5.setText(R.string.debt_to_equity);
        tv6.setText(R.string.return_on_equity);

        TextView tv7 = findViewById(R.id.textView7);
        TextView tv8 = findViewById(R.id.textView8);
        TextView tv9 = findViewById(R.id.textView9);

        tv7.setText(ratiosList.get(0)); // current ratio
        tv8.setText(ratiosList.get(1)); // d/e
        tv9.setText(ratiosList.get(2)); // roe


        TextView tv10 = findViewById(R.id.textView10);
        TextView tv11 = findViewById(R.id.textView11);

        tv10.setText(R.string.return_on_assets);
        tv11.setText(R.string.profit_margin);

        TextView tv12 = findViewById(R.id.textView12);
        TextView tv13 = findViewById(R.id.textView13);

        tv12.setText(ratiosList.get(3)); // roa
        tv13.setText(ratiosList.get(4)); // pm


    }

    public String getValue(String name, int year) {
        /* String value = presenter.getRatio(name, year);
        if (value.length() > 4) {
            return value.substring(0, 5);
        } else {
            return value;
        }*/

        return null;
    }


    public void goToCompanyView() {
        Intent intent = new Intent(this, CompanyActivity.class);
        // intent.putExtra("CURRENT_COMPANY", presenter.getCompany());
        startActivityForResult(intent,1);
    }

    public void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

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