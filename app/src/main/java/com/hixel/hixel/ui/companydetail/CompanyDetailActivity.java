package com.hixel.hixel.ui.companydetail;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import az.plainpie.PieView;
import az.plainpie.animation.PieAngleAnimation;

import com.hixel.hixel.R;
import com.hixel.hixel.data.entities.company.Company;
import com.hixel.hixel.data.entities.user.Ticker;
import com.hixel.hixel.data.entities.user.User;
import com.hixel.hixel.databinding.ActivityCompanyBinding;
import com.hixel.hixel.ui.base.BaseActivity;
import com.hixel.hixel.ui.commonui.HorizontalListViewAdapter;
import com.hixel.hixel.ui.commonui.HorizontalListViewOnClickListener;
import com.hixel.hixel.ui.commonui.GraphFragment;

import com.hixel.hixel.ui.dashboard.DashboardActivity;
import dagger.android.AndroidInjection;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 *  CompanyDetailActivity displays the UI for the details of one company
 *
 */
public class CompanyDetailActivity extends BaseActivity<ActivityCompanyBinding>
        implements HorizontalListViewOnClickListener {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private CompanyDetailViewModel viewModel;

    private GraphFragment fragment;
    private String selectedRatio = "Returns";
    private List<String> tickers = new ArrayList<>();
    private Company currentCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView(R.layout.activity_company);

        setupToolbar(R.string.default_text, true, false);
        setupBottomNavigationView(R.id.home_button);

        String ticker = getIntent().getStringExtra("COMPANY_TICKER");
        fragment = (GraphFragment) getFragmentManager().findFragmentById(R.id.fragment_generic_overtime);

        this.configureDagger();
        this.configureViewModel(ticker);

    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel(String ticker) {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                                      .get(CompanyDetailViewModel.class);
        viewModel.loadCompany(ticker);
        viewModel.loadUser();
        viewModel.getUser().observe(this, this::configureCompany);
    }

    private void configureCompany(User user) {
        if (user != null ) {
            viewModel.getCompany().observe(this, this::updateUI);

            for (Ticker t : user.getPortfolio().getCompanies()) {
                tickers.add(t.getTicker());
            }
        }
    }

    /**
     * Updates the UI on LiveData changes
     * @param company The current company
     */
    private void updateUI(Company company) {
        if (company != null) {

            // Set the toolbar title to the company name
            String title = company.getIdentifiers().getFormattedName();
            setToolbarTitle(title);

            // Setup FAB
            binding.fab.setOnClickListener(v -> {
                viewModel.saveCompany(company);
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.putExtra("QUICK_ADD", company.getIdentifiers().getTicker());
                startActivity(intent);
                finish();
            });

            if (viewModel.isInPortfolio(company.getIdentifiers().getTicker(), tickers)) {
                binding.fab.setVisibility(View.INVISIBLE);
            }

            companyChartSetup(company);
            setupGenericChart(company);
            currentCompany = company;
        }
    }

    /**
     * UI setup for the Company PieChart
     * @param company The current company
     */
    private void companyChartSetup(Company company) {
        PieView pieView = binding.companyPie;

        pieView.setMainBackgroundColor(ContextCompat.getColor(this, R.color.shaded));
        pieView.setTextColor(ContextCompat.getColor(this, R.color.text_main_light));
        pieView.setMainBackgroundColor(
                ContextCompat.getColor(this, R.color.secondary_background));
        pieView.setPieInnerPadding(20);

        float score = (float) company.getDataEntries().get(0).overallScore() * 20;
        pieView.setPercentage(score);
        pieView.setPercentageBackgroundColor(getColorIndicator(score));

        PieAngleAnimation animation = new PieAngleAnimation(pieView);
        animation.setDuration(1500);
        pieView.startAnimation(animation);
    }


    /**
     * UI setup for the historical line chart
     * @param company The current company
     */
    public void setupGenericChart(Company company) {
        RecyclerView recyclerView = findViewById(R.id.ratios_list_view1);
        recyclerView.setHasFixedSize(true);

        LayoutManager layoutManager = new LinearLayoutManager(
                this,LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.Adapter adapter = new HorizontalListViewAdapter(this,  this);
        recyclerView.setAdapter(adapter);

        fragment.drawGraph(company, selectedRatio);
    }

    /**
     * Returns the color based upon the inputted value
     * @param value The 'score' of the Company
     * @return An integer representing the color
     */
    private int getColorIndicator(float value) {
        if (value <= 30.0f) {
            return ContextCompat.getColor(this, R.color.bad);
        } else if (value >= 51.0f) {
            return ContextCompat.getColor(this, R.color.good);
        }

        return ContextCompat.getColor(this, R.color.average);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem searchView = menu.findItem(R.id.action_search);
        searchView.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(String ratio) {
        selectedRatio = ratio;
        fragment.drawGraph(currentCompany, selectedRatio);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
