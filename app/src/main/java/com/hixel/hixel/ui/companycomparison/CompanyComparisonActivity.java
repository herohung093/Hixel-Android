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
import android.support.v7.widget.SearchView.SearchAutoComplete;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.hixel.hixel.R;
import com.hixel.hixel.commonui.DashboardAdapter;
import com.hixel.hixel.commonui.DashboardAdapter.ViewHolder;
import com.hixel.hixel.commonui.HorizontalCompanyListAdapter;
import com.hixel.hixel.data.entities.User;
import com.hixel.hixel.ui.companydetail.CompanyDetailActivity;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.databinding.ActivityComparisonBinding;
import com.hixel.hixel.data.models.SearchEntry;
import com.hixel.hixel.commonui.SearchAdapter;
import com.hixel.hixel.ui.dashboard.DashboardActivity;
import com.hixel.hixel.ui.profile.ProfileActivity;
import dagger.android.AndroidInjection;
import io.reactivex.observers.DisposableObserver;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class CompanyComparisonActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private static final String TAG = CompanyDetailActivity.class.getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private CompanyComparisonViewModel viewModel;

    private ActivityComparisonBinding binding;

    private RecyclerView comparisonCompaniesRecyclerView;
    private RecyclerView dashboardCompaniesRecyclerView;
    private DashboardAdapter comparisonCompaniesAdapter;
    private HorizontalCompanyListAdapter horizontalCompanyListAdapter;
    private Button compareButton;
    private SearchAutoComplete searchAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_comparison);

        comparisonCompaniesRecyclerView = binding.comparisonRecyclerView;
        dashboardCompaniesRecyclerView = binding.dashboardCompRecyclerView;

        compareButton = binding.compareButton;

        this.configureDagger();
        this.configureViewModel();

        viewModel.setupSearch(getSearchObserver());

        dragDownToAdd();
        setupButtons();
        setupSearchView();
        setupBottomNavigationView();
        setupComparisonAdapter();
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CompanyComparisonViewModel.class);
        viewModel.init();
        viewModel.getUser().observe(this, this::updateDashboardCompanies);
        viewModel.getComparisonCompanies().observe(this, this::updateComparisonCompanies);
    }

    public void updateDashboardCompanies(User user) {
        if (user != null) {
            List<String> tickers = user.getPortfolio().getCompanies();

            viewModel.loadDashboardCompanies(tickers);
            viewModel.getDashboardCompanies().observe(this, this::setupDashboardCompanyListAdapter);
        }
    }

    public void updateComparisonCompanies(List<Company> companies) {
        if (companies != null && companies.get(0) != null) {
            comparisonCompaniesAdapter.addCompanies(companies);
        }
    }

    private void setupDashboardCompanyListAdapter(List<Company> companies) {
        if (companies != null) {
            HorizontalCompanyListAdapter horizontalCompanyListAdapter = new HorizontalCompanyListAdapter(companies);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);

            dashboardCompaniesRecyclerView.setAdapter(horizontalCompanyListAdapter);
            dashboardCompaniesRecyclerView.setLayoutManager(layoutManager);
            dashboardCompaniesRecyclerView.setHasFixedSize(true);

            horizontalCompanyListAdapter.setCompanies(companies);
            horizontalCompanyListAdapter.notifyDataSetChanged();
        }
    }

    private void setupComparisonAdapter() {
        comparisonCompaniesAdapter = new DashboardAdapter(this, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        comparisonCompaniesRecyclerView.setLayoutManager(layoutManager);
        comparisonCompaniesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        comparisonCompaniesRecyclerView.setAdapter(comparisonCompaniesAdapter);

        // setup swiping left or right to delete item
        setUpItemTouchHelper();
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
        mItemTouchHelper.attachToRecyclerView(dashboardCompaniesRecyclerView);
    }

    private void setupButtons() {
        compareButton.setOnClickListener((View view) -> {
            Intent moveToGraph = new Intent(this, GraphActivity.class);
            // ArrayList<Company> companies = viewModel.getCompanies().getValue();
            // List<Company> deDupStringList3 = companies.stream().distinct().collect(Collectors.toList());

            // TODO: Some if-statement to make this show only if the user has not selected two companies
            Toast.makeText(getApplicationContext(), "Select at least 2 companies!", Toast.LENGTH_LONG).show();

            // moveToGraph.putExtra("COMPARISON_COMPANIES", (Serializable) deDupStringList3);
            startActivity(moveToGraph);
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
                    final View foreground = ((DashboardAdapter.ViewHolder) viewHolder).getForeground();
                    getDefaultUIUtil().onSelected(foreground);
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view remove it from adapter
                // comparisonCompaniesAdapter.removeItem(viewHolder.getAdapterPosition());
            }
            @Override
             public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final View foreground = ((ViewHolder) viewHolder).getForeground();
                getDefaultUIUtil().clearView(foreground);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView,
                RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                final View foreground = ((ViewHolder) viewHolder).getForeground();
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

    private void setupSearchView() {
        SearchView search = binding.searchView;

        search.setQueryHint("Add companies...");
        search.setFocusable(true);
        search.requestFocus();
        search.requestFocusFromTouch();
        search.setIconified(false);

        searchAutoComplete = search.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        // Styling the search bar
        searchAutoComplete.setHintTextColor(Color.GRAY);
        searchAutoComplete.setTextColor(Color.GRAY);

        searchAutoComplete.setOnItemClickListener((adapterView, view, itemIndex, id) -> {
            SearchEntry entry = (SearchEntry) adapterView.getItemAtPosition(itemIndex);
            String ticker = entry.getTicker();
            searchAutoComplete.setText("");

            viewModel.addToComparisonCompanies(ticker);
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    public void showSearchResults(List<SearchEntry> searchResults) {
        SearchAdapter adapter = new SearchAdapter(this, searchResults);
        searchAutoComplete.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (!searchResults.isEmpty()) {
            searchAutoComplete.showDropDown();
        }
    }
}
