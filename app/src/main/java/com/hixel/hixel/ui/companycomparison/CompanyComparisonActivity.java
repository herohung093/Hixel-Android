package com.hixel.hixel.ui.companycomparison;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.hixel.hixel.R;
import com.hixel.hixel.commonui.HorizontalCompanyListAdapter;
import com.hixel.hixel.data.entities.User;
import com.hixel.hixel.ui.companydetail.CompanyDetailActivity;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.databinding.ActivityComparisonBinding;
import com.hixel.hixel.data.models.SearchEntry;
import com.hixel.hixel.commonui.ComparisonAdapter;
import com.hixel.hixel.commonui.ComparisonAdapter.ViewHolder;
import com.hixel.hixel.commonui.SearchAdapter;
import com.hixel.hixel.ui.dashboard.DashboardActivity;
import com.hixel.hixel.ui.profile.ProfileActivity;
import dagger.android.AndroidInjection;
import io.reactivex.observers.DisposableObserver;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;

public class CompanyComparisonActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private static final String TAG = CompanyDetailActivity.class.getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private CompanyComparisonViewModel viewModel;

    private ActivityComparisonBinding binding;
    private ComparisonAdapter comparisonCompaniesAdapter;
    private RecyclerView userCompaniesRecyclerView;
    private RecyclerView comparisonCompaniesRecyclerView;
    private Button compareButton;

    RecyclerView dashboardCompanyRecycleView;
    SearchView search;
    SearchView.SearchAutoComplete searchAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_comparison);

        userCompaniesRecyclerView = binding.dashboardCompRecyclerView;
        comparisonCompaniesRecyclerView = binding.comparisonRecyclerView;
        compareButton = binding.compareButton;

        this.configureDagger();
        this.configureViewModel();

        setupComparisonAdapter();
        dragDownToAdd();
        setupButtons();
        setupSearchView();
        setupBottomNavigationView();
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CompanyComparisonViewModel.class);
        viewModel.init();
        viewModel.getUser().observe(this, this::updateDashboardCompanies);
    }

    public void updateDashboardCompanies(User user) {
        if (user != null) {
            List<String> tickers = user.getPortfolio().getCompanies();

            viewModel.loadDashboardCompanies(tickers);
            viewModel.getDashboardCompanies().observe(this, this::setupDashboardCompanyListAdapter);
        }
    }

    private void setupDashboardCompanyListAdapter(List<Company> companies) {

        HorizontalCompanyListAdapter userCompaniesAdapter = new HorizontalCompanyListAdapter(companies);
        userCompaniesRecyclerView.setAdapter(userCompaniesAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        userCompaniesRecyclerView.setLayoutManager(layoutManager);
        // userCompaniesRecyclerView.setHasFixedSize(true);

        userCompaniesAdapter.addItems(companies);
    }

    public void setupBottomNavigationView() {
        binding.bottomNavigation.bottomNavigation.setSelectedItemId(R.id.compare_button);
        binding.bottomNavigation.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_button:
                    Intent moveToHome = new Intent(this, DashboardActivity.class);
                    startActivity(moveToHome);
                    break;
                case R.id.profile_button:
                    Intent moveToCompare = new Intent(this, ProfileActivity.class);
                    startActivity(moveToCompare);
                    break;
            }

            return true;
        });
    }

    private void setupComparisonAdapter() {
        comparisonCompaniesAdapter = new ComparisonAdapter(this, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        comparisonCompaniesRecyclerView.setLayoutManager(layoutManager);
        comparisonCompaniesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        comparisonCompaniesRecyclerView.setAdapter(comparisonCompaniesAdapter);

        // setup swiping left or right to delete item
        setUpItemTouchHelper();
    }

    private void dragDownToAdd(){
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.DOWN) {

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                        RecyclerView.ViewHolder target) {
                    return true;
                }

                @Override
                public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                    if (viewHolder != null) {
                        final View cardView = ((HorizontalCompanyListAdapter.ViewHolder) viewHolder).cardView;
                        getDefaultUIUtil().onSelected(cardView);
                    }
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    // TODO: Restore functionality
                    // Row is swiped from recycler view remove it from adapter
                    // final Company temp = (viewModel.getPortfolio().getValue().get(viewHolder.getAdapterPosition()));

                    // selectedCompany.add(temp);
                    // userCompaniesAdapter.removeItem(viewHolder.getAdapterPosition());
                    // comparisonCompaniesAdapter.addItem(temp);
                    // viewModel.addToCompare(temp.getCompanyIdentifiers().getTicker());
                }

                @Override
                public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                    final View cardView = ((HorizontalCompanyListAdapter.ViewHolder) viewHolder).cardView;
                    getDefaultUIUtil().clearView(cardView);
                }

                @Override
                public void onChildDraw(Canvas c, RecyclerView recyclerView,
                    RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    final View cardView = ((HorizontalCompanyListAdapter.ViewHolder) viewHolder).cardView;
                    getDefaultUIUtil().onDraw(c, recyclerView, cardView, dX, dY, actionState, isCurrentlyActive);
                }

                @Override
                public int convertToAbsoluteDirection(int flags, int layoutDirection) {
                    return super.convertToAbsoluteDirection(flags, layoutDirection);
                }
            };

        // attaching the touch helper to recycler view
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        mItemTouchHelper.attachToRecyclerView(dashboardCompanyRecycleView);
    }

    private void setupButtons() {
        compareButton.setOnClickListener((View view) -> {
            Intent moveToGraph = new Intent(this, GraphActivity.class);
            ArrayList<Company> companies = viewModel.getCompanies().getValue();
            List<Company> deDupStringList3 = companies.stream().distinct().collect(Collectors.toList());

            // TODO: Some if-statement to make this show only if the user has not selected two companies
            Toast.makeText(getApplicationContext(), "Select at least 2 companies!", Toast.LENGTH_LONG).show();

            moveToGraph.putExtra("COMPARISON_COMPANIES", (Serializable) deDupStringList3);
            startActivity(moveToGraph);
        });
    }


    private void setupSearchView() {
        search = binding.searchView;

        search.setQueryHint("Add companies...");
        search.setFocusable(true);
        search.requestFocus();
        search.requestFocusFromTouch();
        search.setIconified(false);

        searchAutoComplete = search.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        // Styling the search bar
        searchAutoComplete.setHintTextColor(Color.GRAY);
        searchAutoComplete.setTextColor(Color.GRAY);

        searchAutoComplete.setOnItemClickListener((adapterView, view, i, l) -> {
            SearchEntry entry = (SearchEntry) adapterView.getItemAtPosition(i);
            String ticker = entry.getTicker();
            searchAutoComplete.setText("");

            ArrayList<Company> companies = viewModel.getCompanies().getValue();
            int size = (companies == null) ? 0 : companies.size();

            if (size < 10) {
                // viewModel.addToCompare(ticker);
            }
        });

        this.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
    }

    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if(viewHolder != null) {
                    final View foreground = ((ComparisonAdapter.ViewHolder) viewHolder).foreground;
                    getDefaultUIUtil().onSelected(foreground);
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view remove it from adapter
                comparisonCompaniesAdapter.removeItem(viewHolder.getAdapterPosition());
            }
            @Override
             public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final View foreground = ((ViewHolder) viewHolder).foreground;
                getDefaultUIUtil().clearView(foreground);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView,
                RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                final View foreground = ((ViewHolder) viewHolder).foreground;
                getDefaultUIUtil().onDraw(c, recyclerView, foreground, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public int convertToAbsoluteDirection(int flags, int layoutDirection) {
                return super.convertToAbsoluteDirection(flags, layoutDirection);
            }
        };

        // attaching the touch helper to recycler view
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        mItemTouchHelper.attachToRecyclerView(comparisonCompaniesRecyclerView);
    }

    public void showSearchResults(List<SearchEntry> searchResults) {
        SearchAdapter adapter = new SearchAdapter(this, searchResults);
        searchAutoComplete.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (!searchResults.isEmpty()) {
            searchAutoComplete.showDropDown();
        }
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
}
