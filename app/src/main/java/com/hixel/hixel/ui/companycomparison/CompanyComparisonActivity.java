package com.hixel.hixel.ui.companycomparison;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
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
import com.hixel.hixel.data.entities.company.Company;
import com.hixel.hixel.data.entities.user.Ticker;
import com.hixel.hixel.data.entities.user.User;
import com.hixel.hixel.data.models.SearchEntry;
import com.hixel.hixel.databinding.ActivityComparisonBinding;
import com.hixel.hixel.ui.base.BaseActivity;
import com.hixel.hixel.ui.commonui.CompanyListAdapter;
import com.hixel.hixel.ui.commonui.CompanyListAdapter.ViewHolder;
import com.hixel.hixel.ui.commonui.HorizontalCompanyListOnClickListener;
import com.hixel.hixel.ui.commonui.SearchAdapter;
import dagger.android.AndroidInjection;
import io.reactivex.observers.DisposableObserver;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Entry point for comparing companies. User adds companies to the comparison
 * list and then moves onto the comparison view.
 */
public class CompanyComparisonActivity extends BaseActivity<ActivityComparisonBinding> implements
    HorizontalCompanyListOnClickListener {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private CompanyComparisonViewModel viewModel;

    private RecyclerView comparisonCompaniesRecyclerView;
    private RecyclerView dashboardCompaniesRecyclerView;
    private CompanyListAdapter comparisonCompaniesAdapter;
    private Button compareButton;
    private SearchAutoComplete searchAutoComplete;
    private HorizontalCompanyListAdapter horizontalCompanyListAdapter;

    List<Company> dashboardCompanies;
    List<Company> comparisonCompanies;
    List<Company> selectedCompanies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView(R.layout.activity_comparison);

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
        setupBottomNavigationView(R.id.compare_button);
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CompanyComparisonViewModel.class);
        viewModel.init();
        viewModel.getUser().observe(this, this::updateDashboardCompanies);

        updateComparisonCompanies(viewModel.getCompCompanies());
    }

    /**
     * Gets the current companies in the users portfolio
     *
     * @param user The current user
     */
    public void updateDashboardCompanies(User user) {
        if (user != null) {
            List<String> tickers = new ArrayList<>();

            for (Ticker t : user.getPortfolio().getCompanies()) {
                tickers.add(t.getTicker());
            }

            viewModel.loadDashboardCompanies(tickers);
            viewModel.getDashboardCompanies().observe(this, companiesResource
                    -> setupDashboardCompanyListAdapter(companiesResource == null ? null : companiesResource.data));
            viewModel.getComparisonCompanies().observe(this, this::updateComparisonCompanies);
        }
    }

    /**
     * Updates the companies in the comparison recycler view and the comparisonCompanies variable.
     *
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
     *
     * @param companies The companies in the users portfolio
     */
    private void setupDashboardCompanyListAdapter(List<Company> companies) {
        if (companies != null) {
            horizontalCompanyListAdapter
                    = new HorizontalCompanyListAdapter(companies, this);

            LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

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
            if (comparisonCompaniesAdapter.getDataSet() == null
                || comparisonCompaniesAdapter.getDataSet().size() < 2) {
                Toast.makeText(getApplicationContext(),
                    "Select at least 2 companies!", Toast.LENGTH_LONG).show();
                return;
            }
            Intent moveToGraph = new Intent(this, GraphActivity.class);

            moveToGraph.putStringArrayListExtra("COMPARISON_COMPANIES",
                extractTickers((ArrayList<Company>) comparisonCompaniesAdapter.getDataSet()));
            startActivity(moveToGraph);

        });
    }

    /**
     * Method enables dragging companies from the dashboard recycler view onto the list of companies
     * to compare
     */
    private void dragDownToAdd() {
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
                        final View cardView =
                            ((HorizontalCompanyListAdapter.ViewHolder) viewHolder).cardView;
                        getDefaultUIUtil().onSelected(cardView);
                    }
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                    final Company temp = dashboardCompanies.get(viewHolder.getAdapterPosition());

                    if (!checkDuplicate(comparisonCompaniesAdapter.getDataSet(),
                            temp.getIdentifiers().getTicker())) {
                        selectedCompanies.add(temp);
                        //adapter.notifyDataSetChanged();
                        horizontalCompanyListAdapter.removeItem(viewHolder.getAdapterPosition());
                        comparisonCompaniesAdapter.addItem(temp);
                    }
                }

                @Override
                public void clearView(RecyclerView recyclerView,
                    RecyclerView.ViewHolder viewHolder) {
                    final View cardView =
                        ((HorizontalCompanyListAdapter.ViewHolder) viewHolder).cardView;
                    getDefaultUIUtil().clearView(cardView);
                }

                @Override
                public void onChildDraw(Canvas c, RecyclerView recyclerView,
                    RecyclerView.ViewHolder viewHolder, float dX, float dY,
                    int actionState, boolean isCurrentlyActive) {

                    final View cardView =
                        ((HorizontalCompanyListAdapter.ViewHolder) viewHolder).cardView;

                    getDefaultUIUtil().onDraw(c, recyclerView,
                        cardView, dX, dY, actionState, isCurrentlyActive);
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
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                    RecyclerView.ViewHolder target) {
                    return true;
                }

                @Override
                public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                    if (viewHolder != null) {
                        final View foreground =
                            ((CompanyListAdapter.ViewHolder) viewHolder).getForeground();

                        getDefaultUIUtil().onSelected(foreground);
                    }
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    // Row is swiped from recycler view remove it from adapter
                    // comparisonCompaniesAdapter.removeItem(viewHolder.getAdapterPosition());
                    Company toBeRestoredCompany = comparisonCompaniesAdapter
                        .getItem(viewHolder.getAdapterPosition());
                    Company a = null;
                    for (Company c : selectedCompanies) {
                        if (toBeRestoredCompany.getIdentifiers().getTicker().equalsIgnoreCase(c.getIdentifiers().getTicker())) {
                            horizontalCompanyListAdapter.addItem(toBeRestoredCompany);
                            a = c;
                        }
                    }
                    if (a != null) {
                        selectedCompanies.remove(a);
                    }
                    comparisonCompaniesAdapter.removeItem(viewHolder.getAdapterPosition());
                }

                @Override
                public void clearView(RecyclerView recyclerView,
                    RecyclerView.ViewHolder viewHolder) {
                    final View foreground = ((ViewHolder) viewHolder).getForeground();
                    getDefaultUIUtil().clearView(foreground);
                }

                @Override
                public void onChildDraw(Canvas c, RecyclerView recyclerView,
                    RecyclerView.ViewHolder viewHolder, float dX, float dY,
                    int actionState, boolean isCurrentlyActive) {

                    final View foreground = ((ViewHolder) viewHolder).getForeground();
                    getDefaultUIUtil().onDraw(
                        c, recyclerView, foreground, dX, dY, actionState, isCurrentlyActive);
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
        SearchView search = binding.searchView;

        search.setQueryHint("Add companies...");
        search.setIconifiedByDefault(false);
        search.setIconified(false);
        search.clearFocus();

        searchAutoComplete = search.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        // Styling the search bar
        searchAutoComplete.setHintTextColor(Color.GRAY);
        searchAutoComplete.setTextColor(Color.GRAY);

        searchAutoComplete.setOnItemClickListener((adapterView, view, itemIndex, id) -> {
            SearchEntry entry = (SearchEntry) adapterView.getItemAtPosition(itemIndex);
            String ticker = entry.getTicker();
            ArrayList<String> tickers = new ArrayList<>();
            searchAutoComplete.setText("");
            tickers.add(ticker);
            boolean duplicate = false;
            if (comparisonCompaniesAdapter.getDataSet() != null) {
                duplicate = checkDuplicate(comparisonCompaniesAdapter.getDataSet(), ticker);
            }
            if (!duplicate) {
                List<Company> companies = viewModel.getComparisonCompanies().getValue();
                int size = (companies == null) ? 0 : companies.size();

                if (size <= 10) {
                    viewModel.addToComparisonCompanies(tickers);
                } else {
                    Toast.makeText(this, "Reached comparison limit!", Toast.LENGTH_LONG).show();
                }
            }
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
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };
    }

    /**
     * Method displays the search results in the dropdown of the search view
     *
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

    private boolean checkDuplicate(List<Company> companies, String ticker) {

        for (Company c : companies) {
            if (c.getIdentifiers().getTicker().equalsIgnoreCase(ticker)) {
                Toast.makeText(this, "Company already exist in comparison list",
                    Toast.LENGTH_LONG).show();
                return true;
            }
        }
        return false;
    }

    private ArrayList<String> extractTickers(ArrayList<Company> companies) {
        ArrayList<String> tickers = new ArrayList<>();
        for (Company c : companies) {
            tickers.add(c.getIdentifiers().getTicker());
        }
        return tickers;
    }

    @Override
    public void onClick(Company company) {
        if (!checkDuplicate(comparisonCompaniesAdapter.getDataSet(),
                company.getIdentifiers().getTicker())) {
            selectedCompanies.add(company);
            // adapter.notifyDataSetChanged();

            horizontalCompanyListAdapter.removeItem(company.getIdentifiers().getTicker());
            comparisonCompaniesAdapter.addItem(company);
        }
    }
}