package com.hixel.hixel.search;

import java.util.ArrayList;

public class SearchSuggestion {
    private ArrayList<SearchEntry> mSearchEntries;


    public SearchSuggestion() {
    }

    public void setSearchEntries(ArrayList<SearchEntry> searchEntries) {
        mSearchEntries = searchEntries;
        //
    }

    public ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < mSearchEntries.size(); i++) {
            names.add(mSearchEntries.get(i).getName() + "    " + mSearchEntries.get(i).getExchange() + ": " + mSearchEntries.get(i).getTicker());
            }
        return names;

    }


}

