package com.hixel.hixel.dashboard;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.hixel.hixel.R;
import com.hixel.hixel.comparison.ComparisonActivity;
import com.hixel.hixel.databinding.ActivityDashboardBinding;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements DashboardContract.View,
        OnItemSelectedListener {

    private DashboardContract.Presenter presenter;
    RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityDashboardBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        setSupportActionBar(binding.toolbar.toolbar);
        binding.toolbar.toolbarTitle.setText(R.string.dashboard);

        // Init presenter
        presenter = new DashboardPresenter(this);
        presenter.start();

        Spinner spinner = binding.spinner;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.company_dropdown, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        RecyclerView mRecyclerView = binding.recyclerView;
        mAdapter = new DashboardAdapter(this, presenter);
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        final Intent moveToCompare = new Intent(this,ComparisonActivity.class);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.home_button:

                case R.id.compare_button:
                    startActivity(moveToCompare);

                case R.id.settings_button:
            }

            return true;
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem search = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("enter company...");

        SearchView.SearchAutoComplete searchAutoComplete =
                searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(Color.WHITE);
        searchAutoComplete.setTextColor(Color.WHITE);

        ArrayAdapter<String> newsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        searchAutoComplete.setAdapter(newsAdapter);

        searchAutoComplete.setOnItemClickListener((adapterView, view, itemIndex, id) -> {

            String queryString = (String) adapterView.getItemAtPosition(itemIndex);
            searchAutoComplete.setText("" + queryString.trim().substring(0,queryString.lastIndexOf(' ')));
            Toast.makeText(getApplicationContext(),
                    "Here is what the user submitted" + queryString,Toast.LENGTH_LONG).show();

            newsAdapter.notifyDataSetChanged();

        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                presenter.loadSearchSuggestion(searchAutoComplete.getText().toString());

                ArrayAdapter<String> newsAdapter =
                        new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, presenter.getNames());
                searchAutoComplete.setAdapter(newsAdapter);

                newsAdapter.notifyDataSetChanged();


                return false;
            }
        });

        ImageView searchClose = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchClose.setImageResource(R.drawable.ic_clear);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void setPresenter(@NonNull DashboardContract.Presenter presenter) {
        presenter = presenter;
    }

    @Override
    public void updateRatios(ArrayList<String> ratios1) {

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        presenter.sortCompaniesBy(item);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void portfolioChanged() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
