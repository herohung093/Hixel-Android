package com.hixel.hixel.view.ui;



import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.Menu;
import android.view.View;
import az.plainpie.PieView;
import az.plainpie.animation.PieAngleAnimation;
import com.hixel.hixel.R;
import com.hixel.hixel.databinding.ActivityCompanyBinding;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.view.adapter.HorizontalListViewAdapter;
import com.hixel.hixel.viewmodel.CompanyViewModel;
import java.util.ArrayList;
import java.util.Objects;

public class CompanyActivity extends FragmentActivity implements
     CompanyGenericGraphFragment.OnFragmentInteractionListener {

    CompanyViewModel companyViewModel;
    FloatingActionButton fab;
    ActivityCompanyBinding binding;
    CompanyGenericGraphFragment fragment;
    ArrayList<String> ratios= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company);

        companyViewModel = ViewModelProviders.of(this).get(CompanyViewModel.class);

        setupBottomNavigationView();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        Company company = (Company) Objects.requireNonNull(extras).getSerializable("CURRENT_COMPANY");
        companyViewModel.setCompany(company);

        ArrayList<Company> companies = (ArrayList<Company>) Objects.requireNonNull(extras).getSerializable("PORTFOLIO");
        fragment = (CompanyGenericGraphFragment) getFragmentManager().findFragmentById(R.id.fragment_generic_overtime);
        ratios.add("returns");
        ratios.add("performance");
        ratios.add("health");
        ratios.add("strength");
        ratios.add("Safety");

        // Setup the toolbar
        String title = companyViewModel.getCompany().getValue().getIdentifiers().getName();
        binding.toolbar.toolbar.setTitle(title);
        binding.toolbar.toolbar.setTitleTextColor(Color.WHITE);

        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_close);

      /*  setSupportActionBar(binding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/


        // Setup FAB
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent backIntent = getIntent();
            backIntent.putExtra("COMPANY_ADD", company);
            setResult(RESULT_OK,backIntent);
            finish();
        });

        if (companies != null) {
            for (Company c : companies) {
                if (c.getIdentifiers().getName().equals(companyViewModel.getCompany().getValue().getIdentifiers().getName())) {
                    fab.setVisibility(View.INVISIBLE);
                 }
            }
        }

        companyChartSetup();
        visualizeGenericRatios();
    }


    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.home_button:
                    Intent moveToDashBoard = new Intent(this, DashboardActivity.class);
                    startActivity(moveToDashBoard);
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

    private void companyChartSetup() {
        PieView pieView = findViewById(R.id.company_pie);

        pieView.setMainBackgroundColor(ContextCompat.getColor(this, R.color.shaded));
        pieView.setTextColor(ContextCompat.getColor(this, R.color.text_main_light));
        pieView.setMainBackgroundColor(ContextCompat.getColor(this, R.color.secondary_background));
        pieView.setPieInnerPadding(20);

        companyViewModel.getCompany().observe(this, company -> {
            pieView.setPercentage((float) (company.getRatio("Current Ratio", 2017) + 80));
            pieView.setPercentageBackgroundColor(getColorIndicator((int) (company.getRatio("Current Ratio", 2017) + 80)));
        });

        PieAngleAnimation animation = new PieAngleAnimation(pieView);
        animation.setDuration(1500);

        pieView.startAnimation(animation);
    }

    private void visualizeGenericRatios() {
        companyViewModel.getCompany().observe(this, company -> {
            setupGenericChart(company);
        });
    }
    public void setupGenericChart( Company company) {
        RecyclerView mRecyclerView;
        mRecyclerView = findViewById(R.id.ratios_list_view1);
        mRecyclerView.setHasFixedSize(true);
        LayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter;
        ArrayList<Company> companies = new ArrayList<>();
        companies.add(company);
        mAdapter = new HorizontalListViewAdapter(this,ratios,companies,fragment);
        mRecyclerView.setAdapter(mAdapter);

        fragment.drawGraph(company,ratios.get(0));
    }
    private int getColorIndicator(int value) {
        if (value < 50) {
            return ContextCompat.getColor(this, R.color.bad);
        } else if (value > 60) {
            return ContextCompat.getColor(this, R.color.good);
        }

        return ContextCompat.getColor(this, R.color.average);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
