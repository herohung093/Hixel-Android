package com.hixel.hixel.comparison;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.hixel.hixel.R;
import com.hixel.hixel.comparisonGraph.GraphActivity;

public class ComparisonActivity extends Activity implements ComparisonContract.View {

    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    private ComparisonContract.Presenter cpresenter;
    private android.support.v7.widget.SearchView searchView;
    private SearchView.SearchAutoComplete   searchAutoComplete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison);

        //SearchView searchView= (SearchView) findViewById(R.id.comparisonSearchView);
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

         searchView = (android.support.v7.widget.SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("enter company...");
         searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(Color.BLACK);
        searchAutoComplete.setTextColor(Color.BLACK);
        ArrayAdapter<String> newsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        searchAutoComplete.setAdapter(newsAdapter);
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String queryString=(String)adapterView.getItemAtPosition(i);
                searchAutoComplete.setText("" + queryString);

                String ticker=queryString.trim();
                int spaceIndex=ticker.lastIndexOf(' ');
                String userInput=ticker.substring(spaceIndex).trim();
                //Toast.makeText(getApplicationContext(),"Here is what the user submitted "+userInput,Toast.LENGTH_LONG).show();
                newsAdapter.notifyDataSetChanged();
                int flag=0;
                flag=cpresenter.addToCompare(userInput);
                selectedListChanged();
                if(flag==1){
                    Toast.makeText(getApplicationContext(),"Ticker not found",Toast.LENGTH_LONG).show();
                }else if(flag==2){
                    Toast.makeText(getApplicationContext(),"Reach limit",Toast.LENGTH_LONG).show();
                } else selectedListChanged();
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

                ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, cpresenter.getnames());
                searchAutoComplete.setAdapter(newsAdapter);

                newsAdapter.notifyDataSetChanged();


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
