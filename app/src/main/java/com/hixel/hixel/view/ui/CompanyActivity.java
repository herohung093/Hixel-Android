package com.hixel.hixel.view.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.hixel.hixel.R;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.service.models.SearchEntry;
import com.hixel.hixel.view.adapter.SearchAdapter;
import com.hixel.hixel.viewmodel.CompanyViewModel;
import java.util.ArrayList;
import java.util.Objects;
import com.hixel.hixel.databinding.ActivityCompanyBinding;


public class CompanyActivity extends AppCompatActivity {

    CompanyViewModel companyViewModel;
    SearchView search;
    SearchView.SearchAutoComplete searchAutoComplete;
    FloatingActionButton fab;

    ActivityCompanyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company);


        companyViewModel = ViewModelProviders.of(this).get(CompanyViewModel.class);

        setContentView(R.layout.activity_company);

        setupBottomNavigationView();
        companyViewModel.setupSearch();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        Company company = (Company) Objects.requireNonNull(extras).getSerializable("CURRENT_COMPANY");
        companyViewModel.setCompany(company);

        ArrayList<Company> companies = (ArrayList<Company>) extras.getSerializable("PORTFOLIO");


        // Setup the toolbar
        binding.toolbar.toolbar.setTitle(companyViewModel.getCompany().getValue().getIdentifiers().getName());
        binding.toolbar.toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(binding.toolbar.toolbar);


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
            companyViewModel.loadDataForAParticularCompany(ticker);
            goToCompanyView();


            // call the load to portfolio method from here
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                companyViewModel.loadSearchResults(searchAutoComplete.getText().toString());
                showSearchResults();
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    public void showSearchResults() {
        SearchAdapter adapter = new SearchAdapter(this, companyViewModel.getSearchResults());

        searchAutoComplete.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    public void goToCompanyView() {
        Intent intent = new Intent(this, CompanyActivity.class);
        intent.putExtra("CURRENT_COMPANY", companyViewModel.getCompany().getValue());
        startActivityForResult(intent,1);
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
                    // This screen is yet to be implemented
                    break;
            }

            return true;
        });
    }


}
