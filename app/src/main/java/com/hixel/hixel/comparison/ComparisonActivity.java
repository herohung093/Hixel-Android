package com.hixel.hixel.comparison;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.hixel.hixel.R;
import com.hixel.hixel.comparisonGraph.GraphActivity;
import java.util.ArrayList;

public class ComparisonActivity extends Activity implements ComparisonContract.View {

    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    private ComparisonContract.Presenter cpresenter;
    private android.support.v7.widget.SearchView searchView;
    private SearchView.SearchAutoComplete   searchAutoComplete;
    SwipeController swipeController = new SwipeController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison);

        //SearchView searchView= (SearchView) findViewById(R.id.comparisonSearchView);
        recyclerView = findViewById(R.id.recycleView);
        //ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        //itemTouchhelper.attachToRecyclerView(recyclerView);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback
                = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_clear_24dp);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) getApplicationContext().getResources().getDimension(R.dimen.search_icon_padding);
                initiated = true;
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
                cpresenter.getListCompareCompanies().remove(viewHolder.getAdapterPosition());
                adapter.notifyDataSetChanged();
                Log.d("selected List size*********", String.valueOf(cpresenter.getListCompareCompanies().size()));
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // view the background view
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        final Intent moveToCompare = new Intent(this, GraphActivity.class);

        Button compareButton = findViewById(R.id.compareButton);

        compareButton.setOnClickListener((View view) -> {
            if(cpresenter.getListCompareCompanies().size() == 2) {
                moveToCompare.putExtra("selectedCompanies", (ArrayList) cpresenter.getListCompareCompanies());
                startActivity(moveToCompare);
            }
            Toast.makeText(getApplicationContext(),"please select a company",Toast.LENGTH_LONG);
        });

        Button undoButton = findViewById(R.id.undoButton);

        undoButton.setOnClickListener(view -> {
            cpresenter.removeLastItemFromList();
            selectedListChanged();
        });

        cpresenter = new ComparisonPresenter(this);
        cpresenter.start();
        adapter= new ComparisonAdapter(this,cpresenter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        //android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) findViewById(R.id.action_search);
        //searchView.setQueryHint("enter company...");

        /*
        searchView.setQueryHint("Enter Company Ticker");
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("Here is what the user submitted",query);
                Toast.makeText(getApplicationContext(),"Here is what the user submitted"+query,Toast.LENGTH_LONG).show();
                int flag=0;
                flag=cpresenter.addToCompare(query);
                if(flag==1){
                    Toast.makeText(getApplicationContext(),"Ticker not found",Toast.LENGTH_LONG).show();
                }else if(flag==2){
                    Toast.makeText(getApplicationContext(),"Reach limit",Toast.LENGTH_LONG).show();
                } else selectedListChanged();
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
*/

        searchView = findViewById(R.id.searchView);
        searchView.setQueryHint("enter company...");
        searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(Color.BLACK);
        searchAutoComplete.setTextColor(Color.BLACK);

        ArrayAdapter<String> newsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        searchAutoComplete.setAdapter(newsAdapter);

        searchAutoComplete.setOnItemClickListener((adapterView, view, i, l) -> {
            String queryString=(String)adapterView.getItemAtPosition(i);
            searchAutoComplete.setText("" + queryString);

            String ticker=queryString.trim();
            int spaceIndex=ticker.lastIndexOf(' ');
            String userInput=ticker.substring(spaceIndex).trim();
            //Toast.makeText(getApplicationContext(),"Here is what the user submitted "+userInput,Toast.LENGTH_LONG).show();
            newsAdapter.notifyDataSetChanged();

            int flag = 0;
            flag = cpresenter.addToCompare(userInput);
            selectedListChanged();

            if (flag == 1) {
                Toast.makeText(getApplicationContext(),"Ticker not found",Toast.LENGTH_LONG).show();
            } else if (flag == 2) {
                Toast.makeText(getApplicationContext(),"Reach limit",Toast.LENGTH_LONG).show();
            } else {
                selectedListChanged();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                cpresenter.loadSearchSuggestion(searchAutoComplete.getText().toString());

                ArrayAdapter<String> newsAdapter =
                        new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, cpresenter.getNames());
                searchAutoComplete.setAdapter(newsAdapter);

                newsAdapter.notifyDataSetChanged();


                return false;
            }
        });
 }
    @Override
    public void setPresenter(ComparisonContract.Presenter presenter) {
        this.cpresenter = presenter;
    }

    @Override
    public void selectedListChanged() {
        adapter.notifyDataSetChanged();
    }
}
