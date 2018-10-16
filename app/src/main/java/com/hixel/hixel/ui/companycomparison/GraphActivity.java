package com.hixel.hixel.ui.companycomparison;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.widget.ImageView;

import com.hixel.hixel.R;
import com.hixel.hixel.ui.GraphInterface;
import com.hixel.hixel.ui.commonui.CompanyScoreListAdapter;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.ui.commonui.HorizontalListViewAdapter;
import com.hixel.hixel.ui.dashboard.DashboardActivity;
import com.hixel.hixel.ui.profile.ProfileActivity;

import dagger.android.AndroidInjection;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Displays the Line and Radar charts for the companies being compared.
 */
// TODO: Change to AppCompat not FragmentActivity
// TODO: Databinding
public class GraphActivity extends FragmentActivity implements
        GenericChartFragment.OnFragmentInteractionListener, GraphFragment.OnFragmentInteractionListener {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    GraphViewModel viewModel;

    RecyclerView mRecyclerView, companyRecyclerView;
    RecyclerView.Adapter mAdapter, companyListAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    GraphInterface fragmentA;
    ProgressDialog progressDialog;


    // TODO: Large method, see if it can be refactored sensibly.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        progressDialog = new ProgressDialog(this);

        // TODO: Get rid of warning.
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        ArrayList<String> tickers = getIntent().getStringArrayListExtra("COMPARISON_COMPANIES");

        this.configureDagger();
        this.configureViewModel(tickers);

        // Setup bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.graph_generic_navigator);
        setupBottomNavigationView(bottomNavigationView);

        fragmentA = (GraphInterface) getFragmentManager().findFragmentById(R.id.fragment_bar_chart);

        progressDialog.dismiss();


        ImageView infoButton = findViewById(R.id.imageView3);

        infoButton.setOnClickListener(view -> {
            final Dialog dialog= new Dialog(this);
            dialog.setContentView(R.layout.information_popup_window);

            // TODO: Get rid of warning.
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setTitle("Title...");
            int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
            int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
            dialog.getWindow().setLayout(width,height);
            ImageView close_ib = dialog.findViewById(R.id.popup_ib_close);

            close_ib.setOnClickListener(dialogView -> dialog.dismiss());
            dialog.show();
        });

    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel(List<String> tickers) {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(GraphViewModel.class);
        viewModel.init(tickers);
        viewModel.getCompanies().observe(this, this::updateUI);
    }

    private void updateUI(List<Company> companies) {
        if (companies != null) {
            setupListOfCompanies(companies);
            setupListOfRatios(companies);
        }
    }

    /**
     * Creates a list of companies
     *
     * @param companies The list of companies being compared.
     */
    private void setupListOfCompanies(List<Company> companies) {
        companyRecyclerView = findViewById(R.id.company_list);
        companyRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        companyRecyclerView.setLayoutManager(mLayoutManager);
        companyListAdapter = new CompanyScoreListAdapter(this, companies);
        companyRecyclerView.setAdapter(companyListAdapter);
    }

    /**
     * Sets up a list of ratios for the user to select.
     *
     * @param companies Companies being compared.
     */
    public void setupListOfRatios(List<Company> companies) {
        // TODO: This should be done in an xml file.
        ArrayList<String> spinnerList = new ArrayList<>();
        spinnerList.add("Returns");
        spinnerList.add("Current Ratio");
        spinnerList.add("Performance");
        spinnerList.add("Strength");
        spinnerList.add("Health");
        spinnerList.add("Safety");

        mRecyclerView = findViewById(R.id.ratios_list_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new HorizontalListViewAdapter(this, companies, fragmentA);
        mRecyclerView.setAdapter(mAdapter);

        fragmentA.drawGraph(companies, spinnerList.get(0));

        GenericChartFragment fragmentB = (GenericChartFragment)
                getFragmentManager().findFragmentById(R.id.fragment_radar_chart);

        fragmentB.drawGraph(companies);
    }

    public void setupBottomNavigationView(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setSelectedItemId(R.id.compare_button);
        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.home_button:
                    Intent moveToDashBoard = new Intent(this, DashboardActivity.class);
                    startActivity(moveToDashBoard);
                    break;

                case R.id.compare_button:
                    Intent moveToCompare = new Intent(this,
                            CompanyComparisonActivity.class);
                    startActivity(moveToCompare);
                    break;

                case R.id.profile_button:
                    Intent moveToProfile = new Intent(this,ProfileActivity.class);
                    startActivity(moveToProfile);
                    break;
            }

            return true;
        });
    }
}
