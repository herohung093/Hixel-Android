package com.hixel.hixel.view.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.hixel.hixel.R;
import com.hixel.hixel.databinding.ActivityDashboardBinding;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.service.models.MainBarDataSet;
import com.hixel.hixel.service.models.SearchEntry;
import com.hixel.hixel.service.models.charts.MainBarChartRenderer;
import com.hixel.hixel.service.network.Client;
import com.hixel.hixel.service.network.ServerInterface;
import com.hixel.hixel.view.adapter.DashboardAdapter;
import com.hixel.hixel.view.adapter.SearchAdapter;
import com.hixel.hixel.view.callback.RecyclerItemTouchHelper;
import com.hixel.hixel.view.callback.RecyclerItemTouchHelper.RecyclerItemTouchHelperListener;
import com.hixel.hixel.viewmodel.DashboardViewModel;
import io.reactivex.observers.DisposableObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    @SuppressWarnings("unused")
    private static final String TAG = DashboardActivity.class.getSimpleName();

    DashboardViewModel dashboardViewModel;

    DashboardAdapter dashboardAdapter;
    ActivityDashboardBinding binding;
    RecyclerView mRecyclerView;

    String fileName="CompanyList";
    private BarChart chart;

    SearchView search;
    SearchView.SearchAutoComplete searchAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        dashboardViewModel.setupSearch(getSearchObserver());

        // Setup the toolbar
        binding.toolbar.toolbar.setTitle(R.string.dashboard);
        binding.toolbar.toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(binding.toolbar.toolbar);

        // Setup the list of companies
        mRecyclerView = binding.recyclerView;
        setupDashboardAdapter();

        // Setup the bottom navigation bar
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
        searchAutoComplete.setHintTextColor(ContextCompat.getColor(this, R.color.text_main_light));
        searchAutoComplete.setTextColor(ContextCompat.getColor(this, R.color.text_main_light));
        ImageView searchClose = search
                .findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchClose.setImageResource(R.drawable.ic_clear);

        searchAutoComplete.setOnItemClickListener((adapterView, view, itemIndex, id) -> {
            SearchEntry entry = (SearchEntry) adapterView.getItemAtPosition(itemIndex);
            String ticker = entry.getTicker();

            goToCompanyView(ticker);
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                dashboardViewModel.loadSearchResults(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //TODO: Implement this in pretty much any other way (Brayden, put on your MVVM wizard hat and robe)
    public void goToCompanyView(String ticker) {
        Call<ArrayList<Company>> call = Client.getClient()
                                              .create(ServerInterface.class)
                                              .doGetCompanies(ticker, 1);

        call.enqueue(new Callback<ArrayList<Company>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Company>> call,
                                   @NonNull Response<ArrayList<Company>> response) {

                try {
                    Company company = Objects.requireNonNull(response.body()).get(0);
                    goToCompanyView(company);
                }
                catch (Exception e) { //TODO: Provide user-facing message when this occurs.
                    Log.e("loadDataForAParticularCompany",
                            String.format("Failed to retrieve data for ticker: %s", ticker));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Company>> call, @NonNull Throwable t) {
                // TODO: Add failure handling...
            }
        });
    }

    //TODO: See above.
    public void goToCompanyView(Company company) {
        Intent intent = new Intent(this, CompanyActivity.class);
        Bundle extras = new Bundle();

        ArrayList<Company> companies = dashboardViewModel.getPortfolio().getValue();

        extras.putSerializable("CURRENT_COMPANY", company);
        extras.putSerializable("PORTFOLIO", companies);

        intent.putExtras(extras);
        startActivityForResult(intent,1);

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
                    Intent moveToProfile = new Intent(this,ProfileActivity.class);
                    startActivity(moveToProfile);
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

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 3));
        entries.add(new BarEntry(1, 4));
        entries.add(new BarEntry(2, 1));
        entries.add(new BarEntry(3, 2));
        entries.add(new BarEntry(4, 5));

        ArrayList<String> labels = new ArrayList<>();
        labels.add("Health");
        labels.add("Performance");
        labels.add("Return");
        labels.add("Risk");
        labels.add("Strength");

        MainBarDataSet dataSet = new MainBarDataSet(entries, "");

        int[] colours = {
                ContextCompat.getColor(this, R.color.good),
                ContextCompat.getColor(this, R.color.average),
                ContextCompat.getColor(this, R.color.bad)
        };

        dataSet.setColors(colours);

        BarData data = new BarData(dataSet);

        data.setBarWidth(0.2f);
        data.setDrawValues(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setDrawGridLines(false);

        xAxis.setDrawAxisLine(false);

        xAxis.setTextColor(ContextCompat.getColor(this, R.color.text_secondary_dark));
        xAxis.setTypeface(ResourcesCompat.getFont(this, R.font.roboto_condensed_regular));
        xAxis.setTextSize(12);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(5);

        xAxis.setValueFormatter((value, axis) -> labels.get((int) value));

        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setTextColor(ContextCompat.getColor(this, R.color.text_secondary_dark));
        yAxisLeft.setPosition(YAxisLabelPosition.OUTSIDE_CHART);

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
                new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        dashboardViewModel.getPortfolio().observe(DashboardActivity.this,
                companies -> {dashboardAdapter.addItems(companies);
                
        });

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof DashboardAdapter.ViewHolder) {
            // Get name of removed item
            String name = Objects.requireNonNull(dashboardViewModel.getPortfolio().getValue())
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
            Snackbar snackbar = Snackbar.make(binding.getRoot(), name + " removed from portfolio",
                    Snackbar.LENGTH_LONG);

            snackbar.setAction("UNDO",
                    view -> dashboardAdapter.restoreItem(deletedCompany, deletedIndex));

            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.warning));
            snackbar.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Company mCompanyReturned = ((Company) data.getSerializableExtra("COMPANY_ADD"));
                addItem(mCompanyReturned);
            }
        }
    }

    public void addItem(Company company) {
        dashboardAdapter.addItem(company);
    }

    public void showSearchResults(List<SearchEntry> searchResults) {
        SearchAdapter adapter = new SearchAdapter(this, searchResults);
        searchAutoComplete.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (!searchResults.isEmpty()) {
            searchAutoComplete.showDropDown();
        }
    }

    private DisposableObserver<List<SearchEntry>> getSearchObserver() {
        return new DisposableObserver<List<SearchEntry>>() {
            @Override
            public void onNext(List<SearchEntry> searchResults) {
                showSearchResults(searchResults);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }
}
