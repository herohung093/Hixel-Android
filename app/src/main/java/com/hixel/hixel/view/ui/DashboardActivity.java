package com.hixel.hixel.view.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.hixel.hixel.R;
import com.hixel.hixel.service.models.MainBarChartRenderer;
import com.hixel.hixel.service.models.MainBarDataSet;
import com.hixel.hixel.view.callback.RecyclerItemTouchHelper;
import com.hixel.hixel.view.callback.RecyclerItemTouchHelper.RecyclerItemTouchHelperListener;
import com.hixel.hixel.databinding.ActivityDashboardBinding;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.view.adapter.DashboardAdapter;
import com.hixel.hixel.view.adapter.SearchAdapter;
import com.hixel.hixel.service.models.SearchEntry;
import com.hixel.hixel.viewmodel.DashboardViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DashboardActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    @SuppressWarnings("unused")
    private static final String TAG = DashboardActivity.class.getSimpleName();

    DashboardViewModel dashboardViewModel;

    DashboardAdapter dashboardAdapter;
    ActivityDashboardBinding binding;
    RecyclerView mRecyclerView;

    private BarChart chart;

    SearchView search;
    SearchView.SearchAutoComplete searchAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        dashboardViewModel.setupSearch();

        // Set up the toolbar
        setSupportActionBar(binding.toolbar.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        binding.toolbar.toolbarTitle.setText(R.string.dashboard);

        // Set up the list of companies
        mRecyclerView = binding.recyclerView;
        setupDashboardAdapter();

        // Set up the bottom navigation bar
        setupBottomNavigationView();

        // UI for the chart
        setupChart();
        populateChart();
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
            // dashboardViewModel.loadCompanyFromSearch(ticker);

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
                dashboardViewModel.loadSearchResults(searchAutoComplete.getText().toString());
                showSearchResults();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void showSearchResults() {

        SearchAdapter adapter = new SearchAdapter(this, dashboardViewModel.getSearchResults());

        searchAutoComplete.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) binding.bottomNav;
        bottomNavigationView.getMenu().getItem(0).setChecked(false);
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

    public void setupChart() {
        chart = binding.chart;

        chart.setRenderer(new MainBarChartRenderer(chart, chart.getAnimator(), chart.getViewPortHandler()));

        // Configuring the chart
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setDrawValueAboveBar(false);
        chart.setDrawBarShadow(false);
    }

    public void populateChart() {
        // TODO: get data from portfolio
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 2));
        entries.add(new BarEntry(1 ,4));
        entries.add(new BarEntry(2, 3));
        entries.add(new BarEntry(3, 1));
        entries.add(new BarEntry(4, 5));

        ArrayList<String> labels = new ArrayList<>();
        labels.add("Return");
        labels.add("Performance");
        labels.add("Strength");
        labels.add("Health");
        labels.add("Risk");

        MainBarDataSet dataSet = new MainBarDataSet(entries, "");

        int[] colours = {
                Color.parseColor("#36B37E"),    // good
                Color.parseColor("#FFAB00"),    // average
                Color.parseColor("#FF5630")     // bad
        };

        dataSet.setColors(colours);

        BarData data = new BarData(dataSet);

        data.setBarWidth(0.5f);
        data.setDrawValues(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setDrawGridLines(false);

        xAxis.setDrawAxisLine(false);

        xAxis.setTextColor(Color.GRAY);
        xAxis.setTextSize(12);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(5);

        xAxis.setValueFormatter((value, axis) -> labels.get((int) value));

        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setTextColor(Color.GRAY);
        yAxisLeft.setPosition(YAxisLabelPosition.INSIDE_CHART);
        yAxisLeft.setDrawGridLines(true);
        yAxisLeft.setTextSize(12);
        yAxisLeft.setAxisMaximum(5.0f);
        yAxisLeft.setAxisMinimum(0.0f);
        yAxisLeft.setGranularity(1f); // set interval
        yAxisLeft.setDrawLabels(true);
        yAxisLeft.setDrawAxisLine(false);

        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setEnabled(false);

        chart.setData(data);
        chart.invalidate();
    }

    public void setupDashboardAdapter() {

        dashboardAdapter = new DashboardAdapter(this, new ArrayList<>());
        mRecyclerView.setAdapter(this.dashboardAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(false);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        dashboardViewModel.getPortfolio().observe(DashboardActivity.this,
                companies -> dashboardAdapter.addItems(companies));

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof DashboardAdapter.ViewHolder) {
            // Get name of removed item
            String name = dashboardViewModel.getPortfolio().getValue()
                    .get(viewHolder.getAdapterPosition())
                    .getIdentifiers()
                    .getName();

            // Backup item for undo purposes
            final Company deletedCompany = dashboardViewModel.getPortfolio()
                    .getValue()
                    .get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            dashboardAdapter.removeItem(viewHolder.getAdapterPosition());

            // Remove Company from RecyclerView
            Snackbar snackbar = Snackbar.make(binding.getRoot(), name + " removed from portfolio", Snackbar.LENGTH_LONG);

            snackbar.setAction("UNDO", view -> dashboardAdapter.restoreItem(deletedCompany, deletedIndex));

            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            snackbar.show();
        }
    }

    public void goToCompanyView() {
        Intent intent = new Intent(this, CompanyActivity.class);
        Bundle extras = new Bundle();

        // ArrayList<Company> companies = new ArrayList<>(presenter.getCompanies());

        // extras.putSerializable("CURRENT_COMPANY", presenter.getCompany());
        // extras.putSerializable("PORTFOLIO", companies);

        intent.putExtras(extras);
        startActivityForResult(intent,1);
    }

    public void showLoadingIndicator(final boolean active) {
        final ProgressBar progressBar = binding.progressBar;
        progressBar.setVisibility(active ? View.VISIBLE : View.INVISIBLE);
    }

    public void showLoadingError() {
        // Snackbar.make(binding.getRoot(), "Error loading your portfolio", Snackbar.LENGTH_LONG)
        //         .setAction("RETRY", view -> presenter.loadPortfolio())
        //         .show();
    }

    public void getAddedCompany() {
        if (getIntent().hasExtra("COMPANY_ADD")) {
         // presenter.getCompanies().add((Company) getIntent().getSerializableExtra("COMPANY_ADD"));
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Company mCompanyReturned = ((Company) data.getSerializableExtra("COMPANY_ADD"));
                addItem(mCompanyReturned);
            }
        }
    }

    public void addItem(Company company) {
        dashboardAdapter.addItem(company);
    }
}
