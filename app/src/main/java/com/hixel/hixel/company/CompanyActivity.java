package com.hixel.hixel.company;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.hixel.hixel.dashboard.DashboardActivity;
import com.hixel.hixel.models.Company;

import com.hixel.hixel.search.SearchAdapter;
import com.hixel.hixel.search.SearchEntry;
import io.reactivex.subjects.PublishSubject;
import java.util.ArrayList;
import java.util.List;

public class CompanyActivity extends AppCompatActivity implements CompanyContract.View {

    private CompanyContract.Presenter presenter;

    SearchView search;
    SearchView.SearchAutoComplete searchAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        Company company = (Company) extras.getSerializable("CURRENT_COMPANY");

        ArrayList<Company> companies = (ArrayList<Company>) extras.getSerializable("PORTFOLIO");

        Log.d("COMPANY_ACTIVITY", "" + company.getIdentifiers().getName());

        // Set up the toolbar
        setSupportActionBar(findViewById(R.id.toolbar));

        presenter = new CompanyPresenter(this);

        presenter.setCompany((Company) extras.getSerializable("CURRENT_COMPANY"));

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(presenter.getCompanyName());

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(v -> {
            Intent backIntent = new Intent(CompanyActivity.this, DashboardActivity.class);
            backIntent.putExtra("COMPANY_ADD", company);
            startActivity(backIntent);
        });

        for (Company c : companies) {
            if (c.getIdentifiers().getName().equals(presenter.getCompanyName())) {
                fab.setVisibility(View.INVISIBLE);
            }
        }

        presenter.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem searchView = menu.findItem(R.id.action_search);
        PublishSubject<String> subject = PublishSubject.create();

        search = (SearchView) searchView.getActionView();
        searchAutoComplete = search.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        // Styling the search bar
        searchAutoComplete.setHintTextColor(Color.WHITE);
        searchAutoComplete.setTextColor(Color.WHITE);
        ImageView searchClose = search.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchClose.setImageResource(R.drawable.ic_clear);

        presenter.search(subject);

        ArrayAdapter<String> newsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        searchAutoComplete.setAdapter(newsAdapter);

        searchAutoComplete.setOnItemClickListener((adapterView, view, itemIndex, id) -> {
            SearchEntry entry = (SearchEntry)adapterView.getItemAtPosition(itemIndex);
            String ticker = entry.getTicker();
            presenter.loadDataForAParticularCompany(ticker);

            presenter.setTickerFromSearchSuggestion(ticker);
            // call the load to portfolio method from here
        });

        search.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                subject.onNext(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void showSuggestions(List<SearchEntry> searchEntries) {
        SearchAdapter adapter = new SearchAdapter(this, searchEntries);
        searchAutoComplete.setAdapter(adapter);
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



    }

    public void setPresenter(@NonNull CompanyContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public String getValue(String name, int year) {
        String value = presenter.getRatio(name, year);
        if (value.length() > 4) {
            return value.substring(0, 5);
        } else {
            return value;
        }
    }


    public void goToCompanyView() {
        Intent intent = new Intent(this, CompanyActivity.class);
        intent.putExtra("ticker", presenter.getCompany());
        startActivityForResult(intent,1);
    }

}
