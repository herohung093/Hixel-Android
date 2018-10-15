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
import com.hixel.hixel.ui.commonui.CompanyScoreListAdapter;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.ui.commonui.HorizontalListViewAdapter;
import com.hixel.hixel.ui.dashboard.DashboardActivity;
import com.hixel.hixel.ui.profile.ProfileActivity;
import dagger.android.AndroidInjection;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class GraphActivity extends FragmentActivity {

    @SuppressWarnings("unused")
    private static final String TAG = GraphActivity.class.getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    RecyclerView mRecyclerView, companyRecyclerView;
    RecyclerView.Adapter mAdapter, companyListAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    GraphFragment fragmentA;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        progressDialog = new ProgressDialog(this);
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

        fragmentA = (GraphFragment) getFragmentManager().findFragmentById(R.id.fragment_bar_chart);

        progressDialog.dismiss();


        ImageView infoButton = findViewById(R.id.imageView3);

        infoButton.setOnClickListener(view -> {
            final Dialog dialog= new Dialog(this);
            dialog.setContentView(R.layout.information_popup_window);
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
        GraphViewModel viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(GraphViewModel.class);
        viewModel.init(tickers);
        viewModel.getCompanies().observe(this, this::updateUI);
    }

    private void updateUI(List<Company> companies) {
        setupListOfCompanies(companies);

        ArrayList<String> ratios = new ArrayList<>();
        ratios.add("Current Ratio");

        setupListOfRatios(ratios, companies);
    }

    private void setupListOfCompanies(List<Company> companies) {
        companyRecyclerView = findViewById(R.id.company_list);
        companyRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        companyRecyclerView.setLayoutManager(mLayoutManager);
        companyListAdapter = new CompanyScoreListAdapter(this, companies);
        companyRecyclerView.setAdapter(companyListAdapter);
    }


    public void setupListOfRatios(ArrayList<String> spinnerList, List<Company> companies) {
        mRecyclerView = findViewById(R.id.ratios_list_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new HorizontalListViewAdapter(this, spinnerList, companies, fragmentA);
        mRecyclerView.setAdapter(mAdapter);

        checkUpFinancialEntry(spinnerList);
        fragmentA.drawGraph(companies,spinnerList.get(0));

        GenericChartFragment fragmentB = (GenericChartFragment) getFragmentManager().findFragmentById(R.id.fragment_radar_chart);
        fragmentB.drawGraph(companies);
    }


    // TODO: This is O(n^3), has to be a better way.
    public void checkUpFinancialEntry(ArrayList<String> toBeCheckRatios) {
        /*
        for (Company c : receivedCompanies) {
            for (int i = 0; i<c.getFinancialDataEntries().size(); i++) {
                LinkedHashMap<String, Double> ratiosData = c.getFinancialDataEntries().get(i).getRatios();

                for (String k : toBeCheckRatios) {
                    if (ratiosData.get(k) == null) {
                        Log.d(String.valueOf(c.getFinancialDataEntries().get(i).getYear()) + k + ": ", "NULL***");
                        c.getFinancialDataEntries().get(i).getRatios().put(k,0.0);
                    }
                }
            }
        }*/
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
                    Intent moveToCompare = new Intent(this, CompanyComparisonActivity.class);
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
