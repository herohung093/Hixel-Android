package com.hixel.hixel.view.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.hixel.hixel.R;
import com.hixel.hixel.dashboard.DashboardContract;
import com.hixel.hixel.dashboard.DashboardPresenter;
import com.hixel.hixel.view.callback.RecyclerItemTouchHelper;
import com.hixel.hixel.view.callback.RecyclerItemTouchHelper.RecyclerItemTouchHelperListener;
import com.hixel.hixel.databinding.ActivityDashboardBinding;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.view.adapter.DashboardAdapter;
import com.hixel.hixel.view.adapter.SearchAdapter;
import com.hixel.hixel.service.models.SearchEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DashboardActivity extends AppCompatActivity implements DashboardContract.View,
        RecyclerItemTouchHelperListener, OnItemSelectedListener {

    @SuppressWarnings("unused")
    private static final String TAG = DashboardActivity.class.getSimpleName();
    private DashboardContract.Presenter presenter;

    DashboardAdapter dashboardAdapter;
    ActivityDashboardBinding binding;
    RecyclerView mRecyclerView;
    private RadarChart chart;
    private Company mCompanyReturned;

    SearchView search;
    SearchView.SearchAutoComplete searchAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        // Set up the toolbar
        setSupportActionBar(binding.toolbar.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
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
        setupBottomNavigationView();

        // UI for the chart
        setupChart();

        // Init presenter
        presenter = new DashboardPresenter(this);
        presenter.start();

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
            presenter.loadDataForAParticularCompany(ticker);

            presenter.setTickerFromSearchSuggestion(ticker);
            // call the load to portfolio method from here
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.loadSearchResults(searchAutoComplete.getText().toString());
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void showSearchResults(List<SearchEntry> searchEntries) {
        SearchAdapter adapter = new SearchAdapter(this, searchEntries);

        searchAutoComplete.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(@NonNull DashboardContract.Presenter presenter) {
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        switch(item) {
            case "Sort":
                break;
            case "Current Ratio":
                presenter.sortCompaniesBy(item);
                dashboardAdapter.notifyDataSetChanged();
                break;
            case "Debt-to-Equity":
                presenter.sortCompaniesBy(item + " Ratio");
                dashboardAdapter.notifyDataSetChanged();
                break;
            case "Return-on-Equity":
                presenter.sortCompaniesBy(item + " Ratio");
                dashboardAdapter.notifyDataSetChanged();
                break;
            case "Return-on-Assets":
                presenter.sortCompaniesBy(item + " Ratio");
                dashboardAdapter.notifyDataSetChanged();
                break;
        }
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
        yAxis.setAxisMaximum(1.0f);
        yAxis.setLabelCount(5);
        yAxis.setDrawLabels(false);
    }

    @Override
    public void populateChart() {
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
    }

    @Override
    public void setupDashboardAdapter() {

        dashboardAdapter = new DashboardAdapter(this, presenter);
        mRecyclerView.setAdapter(this.dashboardAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

    }

    // TODO: Make this MVP.
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof DashboardAdapter.ViewHolder) {
            // Get name of removed item
            String name = presenter.getCompanies()
                    .get(viewHolder.getAdapterPosition())
                    .getIdentifiers()
                    .getName();

            // Backup item for undo purposes
            final Company deletedCompany = presenter.getCompanies().get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            dashboardAdapter.removeItem(viewHolder.getAdapterPosition());

            // Remove Company from RecyclerView
            Snackbar snackbar = Snackbar.make(binding.getRoot(), name + " removed from portfolio", Snackbar.LENGTH_LONG);

            snackbar.setAction("UNDO", view -> dashboardAdapter.restoreItem(deletedCompany, deletedIndex));

            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            snackbar.show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void goToCompanyView() {
        Intent intent = new Intent(this, CompanyActivity.class);
        Bundle extras = new Bundle();

        ArrayList<Company> companies = new ArrayList<>(presenter.getCompanies());

        extras.putSerializable("CURRENT_COMPANY", presenter.getCompany());
        extras.putSerializable("PORTFOLIO", companies);

        intent.putExtras(extras);
        startActivityForResult(intent,1);
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

    @Override
    public void getAddedCompany() {
        if (getIntent().hasExtra("COMPANY_ADD")) {
            presenter.getCompanies().add((Company) getIntent().getSerializableExtra("COMPANY_ADD"));
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            if(resultCode==RESULT_OK)
            {
                this.mCompanyReturned= ((Company)data.getSerializableExtra("COMPANY_ADD"));
                addItem(mCompanyReturned);

            }
        }

    }
    public void addItem(Company company)
    {
        dashboardAdapter.addItem(company);
    }

}

