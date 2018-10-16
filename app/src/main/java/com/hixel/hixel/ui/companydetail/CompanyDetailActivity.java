package com.hixel.hixel.ui.companydetail;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.Menu;
import android.view.View;

import az.plainpie.PieView;
import az.plainpie.animation.PieAngleAnimation;

import com.hixel.hixel.R;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.entities.User;
import com.hixel.hixel.databinding.ActivityCompanyBinding;
import com.hixel.hixel.ui.commonui.HorizontalListViewAdapter;
import com.hixel.hixel.ui.companycomparison.CompanyComparisonActivity;
import com.hixel.hixel.ui.dashboard.DashboardActivity;
import com.hixel.hixel.ui.profile.ProfileActivity;

import dagger.android.AndroidInjection;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 *  CompanyDetailActivity displays the UI for the details of one company
 *
 * @author Hixel
 */
public class CompanyDetailActivity extends AppCompatActivity
        implements CompanyGenericGraphFragment.OnFragmentInteractionListener {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private CompanyDetailViewModel viewModel;
    private CompanyGenericGraphFragment fragment;
    private ActivityCompanyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company);

        // General setup
        setupBottomNavigationView();
        binding.toolbar.toolbar.setTitleTextColor(Color.WHITE);
        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_close);

        // TODO: Toolbar needs to be put into a separate file.
        setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String ticker = getIntent().getStringExtra("COMPANY_TICKER");

        this.configureDagger();
        this.configureViewModel(ticker);

        // TODO: Correct way to do this?
        fragment = (CompanyGenericGraphFragment)
                getFragmentManager().findFragmentById(R.id.fragment_generic_overtime);
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel(String ticker) {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                                      .get(CompanyDetailViewModel.class);

        viewModel.init();
        viewModel.getUser().observe(this, (user) -> updateCompany(user, ticker));

    }

    /**
     * Loads the Company
     * @param user The currently active user
     * @param ticker The companies ticker
     */
    private void updateCompany(User user, String ticker) {
        if (user != null) {
            List<String> tickers = user.getPortfolio().getCompanies();
            viewModel.setIsInPortfolio(tickers, ticker);
            viewModel.loadCompany(ticker);
            viewModel.getCompany().observe(this, (company) -> updateUI(company, user));
        }
    }

    /**
     * Updates the UI on LiveData changes
     * @param company The current company
     * @param user The currently active user
     */
    private void updateUI(Company company, User user) {
        if (company != null) {
            // Set the toolbar title to the company name
            String title = company.getFormattedName();
            binding.toolbar.toolbar.setTitle(title);

            // Setup FAB
            binding.fab.setOnClickListener(v -> {
                Intent backIntent = getIntent();
                user.getPortfolio().addCompany(company.getTicker());
                viewModel.saveCompany(company, user);
                setResult(RESULT_OK, backIntent);
                finish();
            });

            if (viewModel.getIsInPortfolio()) {
                binding.fab.setVisibility(View.INVISIBLE);
            }

            companyChartSetup(company);
            setupGenericChart(company);
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

        // TODO: Get an actual 'company overall score' from Company entity
        float score = (float) ((company.getHealthScore() / 5.0) * 100.0);
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

        // TODO: Better way of implementing this
        ArrayList<String> ratios = new ArrayList<>();
        ratios.add("returns");
        ratios.add("performance");
        ratios.add("health");
        ratios.add("strength");
        ratios.add("Safety");

        // TODO: We are putting our single company into an List, needs to change.
        List<Company> companies = new ArrayList<>();
        companies.add(company);

        RecyclerView.Adapter adapter = new HorizontalListViewAdapter(this, companies, fragment);
        recyclerView.setAdapter(adapter);

        fragment.drawGraph(company, ratios.get(0));
    }

    // TODO: Breakpoints need to be in line with confluence ratio proposal

    /**
     * Returns the color based upon the inputted value
     * @param value The 'score' of the Company
     * @return An integer representing the color
     */
    private int getColorIndicator(float value) {
        if (value <= 50.0f) {
            return ContextCompat.getColor(this, R.color.bad);
        } else if (value >= 70.0f) {
            return ContextCompat.getColor(this, R.color.good);
        }

        return ContextCompat.getColor(this, R.color.average);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // TODO: This needs to be in its own file

    /**
     * UI and Logic for the bottom Navigation view
     */
    public void setupBottomNavigationView() {
        binding.bottomNav.bottomNavigation.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.home_button:
                    Intent moveToDashBoard =
                            new Intent(this, DashboardActivity.class);
                    startActivity(moveToDashBoard);
                    break;
                case R.id.compare_button:
                    Intent moveToCompare =
                            new Intent(this, CompanyComparisonActivity.class);
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

    @Override
    public void onFragmentInteraction(Uri uri) { }
}
