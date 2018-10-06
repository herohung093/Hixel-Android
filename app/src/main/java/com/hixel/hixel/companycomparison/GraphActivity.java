package com.hixel.hixel.companycomparison;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.widget.ImageView;
import com.hixel.hixel.R;
import com.hixel.hixel.commonui.HorizontalListViewAdapter;
import com.hixel.hixel.companycomparison.GraphFragment.OnFragmentInteractionListener;
import com.hixel.hixel.dashboard.DashboardActivity;
import com.hixel.hixel.data.models.Company;
import com.hixel.hixel.profile.ProfileActivity;
import java.util.ArrayList;

public class GraphActivity extends FragmentActivity implements OnFragmentInteractionListener {

    @SuppressWarnings("unused")
    private final String TAG = getClass().getSimpleName();

    ArrayList<String> ratios = new ArrayList<>();
    RecyclerView mRecyclerView, companyRecycleView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter,companyListAdapter;
    Intent intentReceiver;
    GraphFragment fragmentA;
    ProgressDialog progressDialog;
    ArrayList<Company> receivedCompanies;
    GraphViewModel graphViewModel;
    ImageView infoButton;

    // ActivityGraphBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        progressDialog = new ProgressDialog(this);
        progressDialog.getWindow().setGravity(Gravity.CENTER);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        intentReceiver = getIntent();
        receivedCompanies = (ArrayList<Company>) intentReceiver.getSerializableExtra("COMPARISON_COMPANIES");
        Context context = this;

        //setup bottom navigator
        BottomNavigationView bottomNavigationView = findViewById(R.id.graph_generic_navigator);
        setupBottomNavigationView(bottomNavigationView);
        fragmentA = (GraphFragment) getFragmentManager().findFragmentById(R.id.fragment_bar_char);

        graphViewModel= ViewModelProviders.of(this).get(GraphViewModel.class);
        //Observe changes in list of ratios

        observeViewModel(graphViewModel);
        setUpListOFCompanies();
        progressDialog.dismiss();

        infoButton = findViewById(R.id.imageView3);
        infoButton.setOnClickListener(view -> {

            final Dialog dialog= new Dialog(context);
            dialog.setContentView(R.layout.information_popup_window);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setTitle("Title...");
            int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
            int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
            dialog.getWindow().setLayout(width,height);
            ImageView close_ib =dialog.findViewById(R.id.popup_ib_close);

            close_ib.setOnClickListener(dialogView -> dialog.dismiss());
            dialog.show();
        });

    }

    private void setUpListOFCompanies() {
        companyRecycleView = findViewById(R.id.company_list);
        companyRecycleView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        companyRecycleView.setLayoutManager(mLayoutManager);
        //companyListAdapter = new CompanyScoreListAdapter(this,receivedCompanies);
        companyRecycleView.setAdapter(companyListAdapter);
    }

    public void setupListOfRatios(ArrayList<String> spinnerList) {
        mRecyclerView = findViewById(R.id.ratios_list_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new HorizontalListViewAdapter(this,spinnerList,receivedCompanies,fragmentA);
        mRecyclerView.setAdapter(mAdapter);
        checkUpFinancialEntry(ratios);
        fragmentA.drawGraph(receivedCompanies,ratios.get(0));
        GenericChartFragment fragmentB = (GenericChartFragment) getFragmentManager().findFragmentById(R.id.fragment_radar_chart);
        //fragmentB.drawGraph(receivedCompanies);
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

                case R.id.settings_button:
                    Intent moveToProfile = new Intent(this,ProfileActivity.class);
                    startActivity(moveToProfile);
                    break;
            }

            return true;
        });
    }

    public void checkUpFinancialEntry(ArrayList<String> toBeCheckRatios) {
        for (Company c : receivedCompanies) {
            /*
            for (int i = 0; i<c.getFinancialDataEntries().size(); i++) {
                LinkedHashMap<String, Double> ratiosData = c.getFinancialDataEntries().get(i).getRatios();

                for (String k : toBeCheckRatios) {
                    if (ratiosData.get(k) == null) {
                        Log.d(String.valueOf(c.getFinancialDataEntries().get(i).getYear()) + k + ": ", "NULL***");
                        c.getFinancialDataEntries().get(i).getRatios().put(k,0.0);
                    }
                }
            }*/
        }
    }
    private void observeViewModel(GraphViewModel graphViewModel){
        graphViewModel.getRatios().observe(this, strings -> {
            if (strings != null) {
                ratios = strings;
                setupListOfRatios(strings);
                checkUpFinancialEntry(strings);
            }
        });
    }
}
