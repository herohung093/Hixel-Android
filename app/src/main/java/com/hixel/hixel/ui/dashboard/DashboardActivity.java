package com.hixel.hixel.ui.dashboard;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.SearchAutoComplete;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.SimpleCallback;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.hixel.hixel.data.entities.User;
import com.hixel.hixel.ui.companycomparison.CompanyComparisonActivity;
import com.hixel.hixel.ui.companydetail.CompanyDetailActivity;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.models.MainBarChartRenderer;
import com.hixel.hixel.data.models.MainBarDataSet;
import com.hixel.hixel.databinding.ActivityDashboardBinding;
import com.hixel.hixel.R;
import com.hixel.hixel.data.models.SearchEntry;
import com.hixel.hixel.commonui.DashboardAdapter;
import com.hixel.hixel.commonui.SearchAdapter;
import com.hixel.hixel.commonui.RecyclerItemTouchHelper;
import com.hixel.hixel.commonui.RecyclerItemTouchHelper.RecyclerItemTouchHelperListener;
import com.hixel.hixel.ui.profile.ProfileActivity;
import dagger.android.AndroidInjection;
import io.reactivex.observers.DisposableObserver;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DashboardActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    @SuppressWarnings("unused")
    private static final String TAG = DashboardActivity.class.getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DashboardViewModel viewModel;

    private ActivityDashboardBinding binding;
    private SearchAutoComplete searchAutoComplete;
    private DashboardAdapter dashboardAdapter;

    private BarChart chart;
    private MainBarDataSet dataSet;
    List<BarEntry> entries;
    BarData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        // Setup the toolbar
        binding.toolbar.toolbar.setTitle(R.string.dashboard);
        binding.toolbar.toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(binding.toolbar.toolbar);

        setupChart();

        this.configureDagger();
        this.configureViewModel();

        viewModel.setupSearch(getSearchObserver());

        setupBottomNavigationView();
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DashboardViewModel.class);
        viewModel.init();
        viewModel.getUser().observe(this, this::updateCompanies);

    }

    public void updateCompanies(User user) {
        if (user != null) {
            List<String> tickers = user.getPortfolio().getCompanies();

            viewModel.loadCompanies(tickers);
            viewModel.getCompanies().observe(this, this::updateUI);
        }
    }

    private void updateUI(List<Company> companies) {
        if (companies != null) {
            binding.progressBar.setVisibility(View.INVISIBLE);
            setupDashboardAdapter(companies);
            updateChart(companies);
        } else {
            // Show Loading indicator
            Log.d(TAG, "updateUI: Loading");
            binding.progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void setupBottomNavigationView() {
        binding.bottomNavigation.bottomNavigation.setSelectedItemId(R.id.home_button);
        binding.bottomNavigation.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.compare_button:
                    Intent moveToCompare = new Intent(this, CompanyComparisonActivity.class);
                    startActivity(moveToCompare);
                    break;
                case R.id.profile_button:
                    Intent moveToProfile = new Intent(this, ProfileActivity.class);
                    startActivity(moveToProfile);
                    break;
            }

            return true;
        });
    }

    public void setupDashboardAdapter(List<Company> companies) {
        RecyclerView recyclerView = binding.recyclerView;

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        dashboardAdapter = new DashboardAdapter(this, companies);
        recyclerView.setAdapter(dashboardAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem searchView = menu.findItem(R.id.action_search);

        SearchView search = (SearchView) searchView.getActionView();
        searchAutoComplete = search.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        // Styling the search bar
        searchAutoComplete.setHintTextColor(ContextCompat.getColor(this, R.color.text_main_light));
        searchAutoComplete.setTextColor(ContextCompat.getColor(this, R.color.text_main_light));

        ImageView searchClose = search.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
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
                viewModel.loadSearchResults(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void goToCompanyView(String ticker) {
        Intent intent = new Intent(this, CompanyDetailActivity.class);
        intent.putExtra("COMPANY_TICKER", ticker);
        startActivity(intent);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof DashboardAdapter.ViewHolder) {
            // Get name of removed item
            String name = viewModel.getCompanies().getValue().get(viewHolder.getAdapterPosition()).getName();

            // Backup item for undo purposes
            final Company deletedCompany = viewModel.getCompanies()
                                                    .getValue()
                                                    .get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            dashboardAdapter.removeItem(viewHolder.getAdapterPosition());

            // Remove Company from RecyclerView
            Snackbar snackbar = Snackbar.make(binding.getRoot(), name + " removed from portfolio", Snackbar.LENGTH_LONG);

            snackbar.setAction("UNDO", view -> dashboardAdapter.restoreItem(deletedCompany, deletedIndex));
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
                this.addItem(mCompanyReturned);
            }
        }
    }

    public void addItem(Company company) {
        dashboardAdapter.addItem(company);
    }

    private DisposableObserver<List<SearchEntry>> getSearchObserver() {
        return new DisposableObserver<List<SearchEntry>>() {
            @Override
            public void onNext(List<SearchEntry> searchResults) {
                showSearchResults(searchResults);
            }

            @Override
            public void onError(Throwable e) { }

            @Override
            public void onComplete() { }
        };
    }

    //TODO: Move the following functions into a an ActivityWithSearch base class.
    public void showSearchResults(List<SearchEntry> searchResults) {
        SearchAdapter adapter = new SearchAdapter(this, searchResults);
        searchAutoComplete.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (!searchResults.isEmpty()) {
            searchAutoComplete.showDropDown();
        }
    }

    private void updateChart(List<Company> companies) {
        float returnsScore = 0.01f;
        float performanceScore = 0.01f;
        float strengthScore = 0.01f;
        float healthScore = 0.01f;
        float safetyScore = 0.01f;
        int size = companies.size();

        for (Company c : companies) {
            returnsScore += c.getReturnsScore();
            performanceScore += c.getPerformanceScore();
            strengthScore += c.getStrengthScore();
            healthScore += c.getHealthScore();
            safetyScore += c.getSafetyScore();
        }


        returnsScore /= size;
        performanceScore /= size;
        strengthScore /= size;
        healthScore /= size;
        safetyScore /= size;

        dataSet.addEntry(new BarEntry(0, returnsScore));
        dataSet.addEntry(new BarEntry(1, performanceScore));
        dataSet.addEntry(new BarEntry(2, strengthScore));
        dataSet.addEntry(new BarEntry(3, healthScore));
        dataSet.addEntry(new BarEntry(4, safetyScore));

        data.notifyDataChanged();
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    private void setupChart() {
        chart = binding.chart;

        entries = new ArrayList<>();
        dataSet = new MainBarDataSet(entries, "");

        chart.setRenderer(new MainBarChartRenderer(chart, chart.getAnimator(), chart.getViewPortHandler()));
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setDrawValueAboveBar(false);
        chart.setDrawBarShadow(false);

        ArrayList<String> labels = new ArrayList<>();
        labels.add("Health");
        labels.add("Performance");
        labels.add("Return");
        labels.add("Safety");
        labels.add("Strength");

        int[] colours = {
                ContextCompat.getColor(this, R.color.good),
                ContextCompat.getColor(this, R.color.average),
                ContextCompat.getColor(this, R.color.bad)
        };

        dataSet.setColors(colours);

        data = new BarData(dataSet);
        data.setBarWidth(0.2f);
        data.setDrawValues(false);


        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(ContextCompat.getColor(this, R.color.text_secondary_dark));
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
}
