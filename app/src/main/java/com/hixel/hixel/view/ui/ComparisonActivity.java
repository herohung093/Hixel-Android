package com.hixel.hixel.view.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.hixel.hixel.R;
import com.hixel.hixel.databinding.ActivityComparisonBinding;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.service.models.SearchEntry;
import com.hixel.hixel.view.adapter.ComparisonAdapter;
import com.hixel.hixel.view.adapter.ComparisonAdapter.ViewHolder;
import com.hixel.hixel.view.adapter.HorizontalCompanyListAdapter;
import com.hixel.hixel.view.adapter.SearchAdapter;
import com.hixel.hixel.viewmodel.ComparisonViewModel;
import io.reactivex.observers.DisposableObserver;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ComparisonActivity extends AppCompatActivity {

    ComparisonAdapter adapter;
    RecyclerView recyclerView, dashboardCompanyRecycleView;
    private Button compareButton;
    SearchView search;
    SearchView.SearchAutoComplete searchAutoComplete;
    ComparisonViewModel comparisonViewModel;
    ActivityComparisonBinding binding;
    HorizontalCompanyListAdapter horizontalCompanyListAdapter;
    private static final String TAG = ComparisonActivity.class.getSimpleName();
    ArrayList<Company> portfolioCompanies, selectedCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comparison);
        comparisonViewModel = ViewModelProviders.of(this).get(ComparisonViewModel.class);
        observeViewModel(comparisonViewModel);

        selectedCompany = new ArrayList<>();
        portfolioCompanies = new ArrayList<>();
        recyclerView = binding.recycleView;
        dashboardCompanyRecycleView = binding.dashboardCompRecycleView;
        setupDashboardCompanyListAdapter();
        compareButton = binding.compareButton;
        setupButtons();
        setupListViewAdapter(); //setup recycle list view
        dragDownToAdd();
        setupSearchView(); //setup search view and search suggestion
        setupBottomNavigationView((BottomNavigationView) binding.bottomNavCompariton);
    }

    private void setupDashboardCompanyListAdapter() {
        horizontalCompanyListAdapter =
            new HorizontalCompanyListAdapter(this, new ArrayList<>());
        LinearLayoutManager mLayoutManager =
            new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        dashboardCompanyRecycleView.setHasFixedSize(true);
        dashboardCompanyRecycleView.setLayoutManager(mLayoutManager);
        dashboardCompanyRecycleView.setAdapter(horizontalCompanyListAdapter);
        comparisonViewModel.getPortfolio().observe(ComparisonActivity.this,
            companies -> {
                horizontalCompanyListAdapter.setCompanies(companies);
                portfolioCompanies = companies;
                horizontalCompanyListAdapter.notifyDataSetChanged();
            });

    }

    private void dragDownToAdd() {
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.DOWN) {

                Drawable background;
                Drawable xMark;

                boolean initiated;

                private void init() {
                    background = new ColorDrawable(Color.RED);
                    xMark = ContextCompat
                        .getDrawable(getApplicationContext(), R.drawable.ic_clear_24dp);

                    if (xMark != null) {
                        xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                    }

                    initiated = true;
                }

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
                    // Row is swiped from recycler view
                    // remove it from adapter
/*                    final Company temp=(comparisonViewModel.getPortfolio().getValue().get(viewHolder.getAdapterPosition()));
                    final int deletedIndex = viewHolder.getAdapterPosition();
                    Log.d(TAG,"@@@@@"+deletedIndex+temp.getIdentifiers().getName().toString());
                    selectedCompany.add(temp);
                    horizontalCompanyListAdapter.removeItem(viewHolder.getAdapterPosition());
                    adapter.addItem(temp);
                    comparisonViewModel.addToCompare(temp.getIdentifiers().getTicker());*/
                    final Company temp = portfolioCompanies.get(viewHolder.getAdapterPosition());
                    final int deletedIndex = viewHolder.getAdapterPosition();
                    Log.d(TAG, "@@@@@" + deletedIndex + temp.getIdentifiers().getName().toString());
                    if (checkDuplicate(adapter.getDataSet(), temp.getIdentifiers().getTicker())
                        == false) {
                        selectedCompany.add(temp);
                        //adapter.notifyDataSetChanged();
                        horizontalCompanyListAdapter.removeItem(viewHolder.getAdapterPosition());
                        adapter.addItem(temp);
                    }
                }

                @Override
                public void clearView(RecyclerView recyclerView,
                    RecyclerView.ViewHolder viewHolder) {
                    final View cardView = ((HorizontalCompanyListAdapter.ViewHolder) viewHolder).cardView;
                    getDefaultUIUtil().clearView(cardView);

                }

                @Override
                public void onChildDraw(Canvas c, RecyclerView recyclerView,
                    RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState,
                    boolean isCurrentlyActive) {
                    final View cardView = ((HorizontalCompanyListAdapter.ViewHolder) viewHolder).cardView;
                    getDefaultUIUtil()
                        .onDraw(c, recyclerView, cardView, dX, dY, actionState, isCurrentlyActive);

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
            Log.d(TAG, "Selected companies size " + selectedCompany.size());
            if (adapter.getDataSet() == null || adapter.getDataSet().size() < 2) {
                Toast.makeText(getApplicationContext(), "Select at least 2 companies!",
                    Toast.LENGTH_LONG).show();
                return;
            }

            moveToGraph.putExtra("COMPARISON_COMPANIES",
                (Serializable) adapter.getDataSet());

            startActivity(moveToGraph);
        });

    }

    private void setupSearchView() {

        search = binding.searchView;
        search.setQueryHint("Add companies...");

        searchAutoComplete = search
            .findViewById(android.support.v7.appcompat.R.id.search_src_text);
        search.requestFocus();
        search.requestFocusFromTouch();
        searchAutoComplete = search.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        // Styling the search bar
        searchAutoComplete.setHintTextColor(Color.GRAY);
        searchAutoComplete.setTextColor(Color.GRAY);

        searchAutoComplete.setOnItemClickListener((adapterView, view, i, l) -> {
            SearchEntry entry = (SearchEntry) adapterView.getItemAtPosition(i);
            String ticker = entry.getTicker();
            boolean duplicate = false;
            if(adapter.getDataSet()!=null){
                duplicate=checkDuplicate(adapter.getDataSet(),ticker);
            }
            if (!duplicate) {
                searchAutoComplete.setText("");
                ArrayList<Company> companies = comparisonViewModel.getCompanies().getValue();
                int size = (companies == null) ? 0 : companies.size();

                if (size <= 10) {
                    comparisonViewModel.addToCompare(ticker);
                } else {
                    Toast.makeText(this, "Can only compare less than companies!", Toast.LENGTH_LONG)
                        .show();
                }
            }
        });

        this.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                comparisonViewModel.loadSearchResults(newText);
                return false;
            }
        });
    }

    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                Drawable background;
                Drawable xMark;
                int xMarkMargin;
                boolean initiated;

                private void init() {
                    background = new ColorDrawable(Color.RED);
                    xMark = ContextCompat
                        .getDrawable(getApplicationContext(), R.drawable.ic_clear_24dp);

                    if (xMark != null) {
                        xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                    }

                    initiated = true;
                }

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                    RecyclerView.ViewHolder target) {
                    return true;
                }

                @Override
                public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                    if (viewHolder != null) {
                        final View foreground = ((ComparisonAdapter.ViewHolder) viewHolder).foreground;
                        getDefaultUIUtil().onSelected(foreground);
                    }
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    // Row is swiped from recycler view
                    // remove it from adapter
                    Company toBeRestoredCompany = adapter.getItem(viewHolder.getAdapterPosition());
                    Company a=null;
                    for(Company c: selectedCompany){
                       if( toBeRestoredCompany.getIdentifiers().getTicker().equalsIgnoreCase(c.getIdentifiers().getTicker())){
                           horizontalCompanyListAdapter.addItem(toBeRestoredCompany);
                           a=c;
                       }
                    }
                    if(a!=null){
                        selectedCompany.remove(a);                    }
                    adapter.removeItem(viewHolder.getAdapterPosition());

                }

                @Override
                public void clearView(RecyclerView recyclerView,
                    RecyclerView.ViewHolder viewHolder) {
                    final View foreground = ((ViewHolder) viewHolder).foreground;
                    getDefaultUIUtil().clearView(foreground);

                }

                @Override
                public void onChildDraw(Canvas c, RecyclerView recyclerView,
                    RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState,
                    boolean isCurrentlyActive) {
                    final View foreground = ((ViewHolder) viewHolder).foreground;
                    getDefaultUIUtil().onDraw(c, recyclerView, foreground, dX, dY, actionState,
                        isCurrentlyActive);

                }

                @Override
                public int convertToAbsoluteDirection(int flags, int layoutDirection) {
                    return super.convertToAbsoluteDirection(flags, layoutDirection);
                }

            };

        // attaching the touch helper to recycler view
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void setupBottomNavigationView(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.getMenu().getItem(0).setChecked(false);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.home_button:
                    Intent moveToDashBoard = new Intent(this, DashboardActivity.class);
                    startActivity(moveToDashBoard);
                    break;
                case R.id.compare_button:
                    // Already here
                    break;
                case R.id.settings_button:
                    Intent moveToProfile = new Intent(this, ProfileActivity.class);
                    startActivity(moveToProfile);
                    break;
            }

            return true;
        });
    }

    public void setupListViewAdapter() {
        adapter = new ComparisonAdapter(this, selectedCompany);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        // setup swiping left or right to delete item
        setUpItemTouchHelper();
    }

    private void observeViewModel(ComparisonViewModel viewModel) {
        viewModel.getCompanies().observe(this, companies -> {
            adapter.setCompanies(companies);
        });
        viewModel.setupSearch(getSearchObserver());
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
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    private boolean checkDuplicate(List<Company> companies, String ticker) {

        for (Company c : companies) {
            if (c.getIdentifiers().getTicker().equalsIgnoreCase(ticker)
                == true) {
                Toast.makeText(this, "Company already exist in comparion list",
                    Toast.LENGTH_LONG).show();
                return true;
            }
        }
        return false;
    }
}
