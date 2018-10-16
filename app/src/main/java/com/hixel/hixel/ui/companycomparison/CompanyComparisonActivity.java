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
import com.hixel.hixel.ui.commonui.CompanyListAdapter;
import com.hixel.hixel.ui.commonui.CompanyListAdapter.ViewHolder;
import com.hixel.hixel.ui.commonui.HorizontalCompanyListAdapter;
import com.hixel.hixel.data.entities.User;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.databinding.ActivityComparisonBinding;
import com.hixel.hixel.data.models.SearchEntry;
import com.hixel.hixel.ui.commonui.SearchAdapter;
import com.hixel.hixel.ui.dashboard.DashboardActivity;
import com.hixel.hixel.ui.profile.ProfileActivity;
import dagger.android.AndroidInjection;
import io.reactivex.observers.DisposableObserver;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Entry point for comparing companies. User adds companies to the comparison
 * list and then moves onto the comparison view.
 */
public class CompanyComparisonActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private CompanyComparisonViewModel viewModel;

    private ActivityComparisonBinding binding;

    private RecyclerView comparisonCompaniesRecyclerView;
    private RecyclerView dashboardCompaniesRecyclerView;
    private CompanyListAdapter comparisonCompaniesAdapter;
    private Button compareButton;
    private SearchAutoComplete searchAutoComplete;

    // TODO: Change to List
    ArrayList<String> tickers = new ArrayList<>();
    List<Company> dashboardCompanies;
    List<Company> comparisonCompanies;

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

        updateComparisonCompanies(viewModel.getCompCompanies());
    }

    /**
     * Gets the current companies in the users portfolio
     * @param user The current user
     */
    public void updateDashboardCompanies(User user) {
        if (user != null) {
            List<String> tickers = user.getPortfolio().getCompanies();

            viewModel.loadDashboardCompanies(tickers);
            viewModel.getDashboardCompanies().observe(this, this::setupDashboardCompanyListAdapter);
            viewModel.getComparisonCompanies().observe(this, this::updateComparisonCompanies);
        }
    }

    /**
     * Updates the companies in the comparison recycler view and the comparisonCompanies
     * variable.
     * @param companies The list of companies for the comparison recyclerView
     */
    public void updateComparisonCompanies(List<Company> companies) {
        if (companies != null && companies.size() >= 1) {
            comparisonCompaniesAdapter.setCompanies(companies);

            comparisonCompanies = viewModel.getCompCompanies();
        }
    }

    /**
     * Sets up the list of companies currently in the users portfolio.
     * @param companies The companies in the users portfolio
     */
    private void setupDashboardCompanyListAdapter(List<Company> companies) {
        if (companies != null) {
            HorizontalCompanyListAdapter horizontalCompanyListAdapter = new HorizontalCompanyListAdapter(companies);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);

            dashboardCompaniesRecyclerView.setAdapter(horizontalCompanyListAdapter);
            dashboardCompaniesRecyclerView.setLayoutManager(layoutManager);
            dashboardCompaniesRecyclerView.setHasFixedSize(true);

            horizontalCompanyListAdapter.setCompanies(companies);
            horizontalCompanyListAdapter.notifyDataSetChanged();

            dashboardCompanies = companies;
        }
    }

    /**
     * Method sets up the adapter layout and swiping interactions.
     */
    private void setupComparisonAdapter() {
        comparisonCompaniesAdapter = new CompanyListAdapter(this, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        comparisonCompaniesRecyclerView.setLayoutManager(layoutManager);
        comparisonCompaniesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        comparisonCompaniesRecyclerView.setAdapter(comparisonCompaniesAdapter);

        // setup swiping left or right to delete item
        setUpItemTouchHelper();
    }

    /**
     * Method adds an onClickListener to the compare button to move to the compare charts
     */
    // TODO: Better name.
    private void setupButtons() {
        compareButton.setOnClickListener((View view) -> {
            if (tickers.size() < 2) {
                Toast.makeText(getApplicationContext(),
                        "Select at least 2 companies!", Toast.LENGTH_LONG).show();
            } else {
                Intent moveToGraph = new Intent(this, GraphActivity.class);
                moveToGraph.putStringArrayListExtra("COMPARISON_COMPANIES", tickers);
                startActivity(moveToGraph);
            }
        });
    }

    /**
     * Method sets up the bottom navigation view
     */
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

    /**
     * Method enables dragging companies from the dashboard recycler view onto
     * the list of companies to compare
     */
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
                    int position = viewHolder.getAdapterPosition();
                    tickers.add(dashboardCompanies.get(position).getTicker());
                    viewModel.addToComparisonCompanies(tickers);
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

    /**
     * Method handles swiping to delete on the Comparison recycler view
     */
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
                    final View foreground = ((CompanyListAdapter.ViewHolder) viewHolder).getForeground();
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

    /**
     * Method sets up the main search view
     */
    private void setupSearchView() {
        // TODO: Figure out how to use databinding for search
        SearchView search = binding.searchView;

        search.setQueryHint("Add companies...");
        search.setIconifiedByDefault(false);
        search.setIconified(false);
        search.clearFocus();

        searchAutoComplete = search.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        // Styling the search bar
        // TODO: Use Hixel styles.
        searchAutoComplete.setHintTextColor(Color.GRAY);
        searchAutoComplete.setTextColor(Color.GRAY);

        searchAutoComplete.setOnItemClickListener((adapterView, view, itemIndex, id) -> {
            SearchEntry entry = (SearchEntry) adapterView.getItemAtPosition(itemIndex);
            String ticker = entry.getTicker();

            searchAutoComplete.setText("");
            tickers.add(ticker);
            viewModel.addToComparisonCompanies(tickers);
            search.clearFocus();
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

    /**
     * Method used to provide an Observer for searching companies.
     *
     * @return DisposableObserver List of Search Entries.
     */
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

    /**
     * Method displays the search results in the dropdown of the search view
     * @param searchResults The list of search results
     */
    public void showSearchResults(List<SearchEntry> searchResults) {
        SearchAdapter adapter = new SearchAdapter(this, searchResults);
        searchAutoComplete.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (!searchResults.isEmpty()) {
            searchAutoComplete.showDropDown();
        }
    }
}
