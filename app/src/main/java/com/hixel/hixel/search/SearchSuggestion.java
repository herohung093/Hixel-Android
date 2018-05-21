package com.hixel.hixel.search;

import java.util.ArrayList;
import java.util.List;

public class SearchSuggestion {
    private List<SearchEntry> mSearchEntries;

    public SearchSuggestion() {
    }

    public void setSearchEntries(ArrayList<SearchEntry> searchEntries) {
        mSearchEntries = searchEntries;
    }


    public List<String> getNames() {
        List<String> names = new ArrayList<>();

        for (int i = 0; i < mSearchEntries.size(); i++) {
            names.add(mSearchEntries.get(i).getName() + "    " + mSearchEntries.get(i).getExchange() + ":" + mSearchEntries.get(i).getTicker());
        }


        return names;
    }
}

