package com.hixel.hixel.view.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.hixel.hixel.R;
import com.hixel.hixel.databinding.ActivityComparisonBinding;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.service.models.SearchEntry;
import com.hixel.hixel.view.adapter.ComparisonAdapter;
import com.hixel.hixel.view.adapter.ComparisonAdapter.ViewHolder;
import com.hixel.hixel.view.adapter.SearchAdapter;
import com.hixel.hixel.viewmodel.ComparisonViewModel;
import io.reactivex.observers.DisposableObserver;
import java.util.ArrayList;
import java.util.List;

public class ComparisonActivity extends AppCompatActivity {

    ComparisonAdapter adapter;
    RecyclerView recyclerView;
    private Button compareButton;
    TextView textView;
    SearchView search;
    SearchView.SearchAutoComplete searchAutoComplete;
    ComparisonViewModel comparisonViewModel;
    ActivityComparisonBinding binding;

    private static final String TAG = CompanyActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_comparison);
        comparisonViewModel = ViewModelProviders.of(this).get(ComparisonViewModel.class);
        observeViewModel(comparisonViewModel);

        recyclerView = binding.recycleView;
        compareButton = binding.compareButton;
        textView = binding.textView;

        setupButtons();
        setupListViewAdapter(); //setup recycle list view
        setupSearchView(); //setup search view and search suggestion
        setupBottomNavigationView((BottomNavigationView)binding.bottomNavCompariton);
    }

    private void setupButtons() {
        compareButton.setOnClickListener((View view) -> {
            Intent moveToGraph = new Intent(this, GraphActivity.class);

            ArrayList<Company> companies = comparisonViewModel.getCompanies().getValue();

            if (companies == null || companies.size() < 2) {
                Toast.makeText(getApplicationContext(), "Select 2 companies first!", Toast.LENGTH_LONG).show();
                return;
            }

            moveToGraph.putExtra("COMPARISON_COMPANIES",
                comparisonViewModel.getCompanies().getValue());

            startActivity(moveToGraph);
        });

    }

    private void setupSearchView() {

        search = binding.searchView;
        search.setQueryHint("Add companies...");

        searchAutoComplete = search
            .findViewById(android.support.v7.appcompat.R.id.search_src_text);
        search.setFocusable(true);
        search.requestFocus();
        search.requestFocusFromTouch();
        search.setIconified(false);

        searchAutoComplete = search.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        // Styling the search bar
        searchAutoComplete.setHintTextColor(Color.GRAY);
        searchAutoComplete.setTextColor(Color.GRAY);
        ImageView searchClose = search.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchClose.setImageResource(R.drawable.ic_clear_black);

        searchAutoComplete.setOnItemClickListener((adapterView, view, i, l) -> {
            SearchEntry entry = (SearchEntry)adapterView.getItemAtPosition(i);
            String ticker = entry.getTicker();
            searchAutoComplete.setText("");

            ArrayList<Company> companies = comparisonViewModel.getCompanies().getValue();
            int size = (companies == null) ? 0 : companies.size();

            if (size < 2) {
                comparisonViewModel.addToCompare(ticker);
            }
            else {
                Toast.makeText(this, "Can only compare 2 companies!", Toast.LENGTH_LONG)
                     .show();
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
                xMark = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_clear_24dp);

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
                if(viewHolder != null) {
                    final View foreground = ((ComparisonAdapter.ViewHolder) viewHolder).foreground;
                    getDefaultUIUtil().onSelected(foreground);
                    }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
                adapter.removeItem(viewHolder.getAdapterPosition());
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
                    Intent moveToProfile = new Intent(this,ProfileActivity.class);
                    startActivity(moveToProfile);
                    break;
            }

            return true;
        });
    }

    public void setupListViewAdapter(){
        adapter = new ComparisonAdapter(this,comparisonViewModel.getCompanies().getValue());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        // setup swiping left or right to delete item
        setUpItemTouchHelper();
    }

    private void observeViewModel(ComparisonViewModel viewModel){
        viewModel.getCompanies().observe(this, companies -> adapter.setCompanies(companies));
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
}
