package com.hixel.hixel.view.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.hixel.hixel.R;
import com.hixel.hixel.comparison.ComparisonContract;
import com.hixel.hixel.comparison.ComparisonPresenter;
import com.hixel.hixel.view.adapter.ComparisonAdapter;
import com.hixel.hixel.view.adapter.SearchAdapter;
import com.hixel.hixel.service.models.SearchEntry;
import java.util.ArrayList;
import java.util.List;

public class ComparisonActivity extends Activity implements ComparisonContract.View {

    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    private ComparisonContract.Presenter presenter;
    private Button compareButton;

    SearchView search;
    SearchView.SearchAutoComplete searchAutoComplete;

    private static final String TAG = CompanyActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison);

        recyclerView = findViewById(R.id.recycleView);
        compareButton = findViewById(R.id.compareButton);

        setupButtons();

        // setup presenter
        presenter = new ComparisonPresenter(this);
        presenter.start();

        //setup recycle list view
        adapter = new ComparisonAdapter(this, presenter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        //setup search view and search suggestion
        setupSearchView();

        //setup bottom navigator
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_compariton);
        setupBottomNavigationView(bottomNavigationView);

        // setup swiping left or right to delete item
        setUpItemTouchHelper();
    }

    private void setupButtons() {
        compareButton.setOnClickListener((View view) -> {
            Intent moveToGraph = new Intent(this, GraphActivity.class);

            if (presenter.getListCompareCompanies().size() < 2) {
                Toast.makeText(getApplicationContext(), "Select 2 companies first!", Toast.LENGTH_LONG).show();
                return;
            }

            moveToGraph.putExtra("COMPARISON_COMPANIES",
                (ArrayList) presenter.getListCompareCompanies());

            startActivity(moveToGraph);
        });

    }

    private void setupSearchView() {
        search = findViewById(R.id.searchView);
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
        searchClose.setImageResource(R.drawable.ic_clear);

        searchAutoComplete.setOnItemClickListener((adapterView, view, i, l) -> {
            SearchEntry entry = (SearchEntry)adapterView.getItemAtPosition(i);
            String ticker = entry.getTicker();
            searchAutoComplete.setText(entry.getName());

            presenter.addToCompare(ticker);
            Log.d(TAG, "COMPANY SIZE: " + String.valueOf(presenter.getListCompareCompanies().size()));
            searchAutoComplete.setText("",false);
            searchAutoComplete.dismissDropDown();

        });

        this.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.loadSearchResults(searchAutoComplete.getText().toString());
                return false;
            }
        });
    }

    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

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

                xMarkMargin = (int) getApplicationContext().getResources()
                    .getDimension(R.dimen.search_icon_padding);
                initiated = true;

            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
                presenter.getListCompareCompanies().remove(viewHolder.getAdapterPosition());
                adapter.notifyDataSetChanged();
                Log.d(TAG,  "selected List size: " +
                        String.valueOf(presenter.getListCompareCompanies().size()));
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView,
                RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                // view the background view
                View itemView = viewHolder.itemView;

                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background

                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(),
                        itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState,
                    isCurrentlyActive);
            }

        };

        // attaching the touch helper to recycler view
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void setPresenter(ComparisonContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void selectedListChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showSearchResults(List<SearchEntry> searchEntries) {
        SearchAdapter adapter = new SearchAdapter(this, searchEntries);

        searchAutoComplete.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void userNotification(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
                    // right here
                    break;
                case R.id.settings_button:
                    // This screen is yet to be implemented
                    break;
            }

            return true;
        });
    }
}
