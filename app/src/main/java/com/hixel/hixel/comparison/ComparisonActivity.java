package com.hixel.hixel.comparison;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.hixel.hixel.R;
import com.hixel.hixel.comparisonGraph.GraphActivity;

public class ComparisonActivity extends Activity implements ComparisonContract.View {

    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    private ComparisonContract.Presenter cpresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison);
        SearchView searchView= (SearchView) findViewById(R.id.comparisonSearchView);
        recyclerView=(RecyclerView) findViewById(R.id.recycleView);

        final Intent moveToCompare= new Intent(this, GraphActivity.class);


        Button compareButton = (Button) findViewById(R.id.compareButton);
        compareButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(cpresenter.getListCompareCompanies().size()==2)
                {
                    moveToCompare.putExtra("selectedCompanies",cpresenter.getListCompareCompanies());
                    startActivity(moveToCompare);
                }
                Toast.makeText(getApplicationContext(),"please select a company",Toast.LENGTH_LONG);
            }
        });
        Button undoButton =(Button) findViewById(R.id.undoButton);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cpresenter.removeLastItemFromList();
                selectedListChanged();
            }
        });
        cpresenter = new ComparisonPresenter(this);
        cpresenter.start();
        adapter= new ComparisonAdapter(this,cpresenter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        searchView.setQueryHint("Enter Company Ticker");
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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

            }



    @Override
    public void setPresenter(ComparisonContract.Presenter presenter) {
        this.cpresenter=presenter;
    }

    @Override
    public void selectedListChanged() {
        adapter.notifyDataSetChanged();
    }
}
