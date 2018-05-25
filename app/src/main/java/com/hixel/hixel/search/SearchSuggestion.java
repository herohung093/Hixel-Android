package com.hixel.hixel.search;

import java.util.ArrayList;
import java.util.List;

public class SearchSuggestion {
    private List<SearchEntry> mSearchEntries;

    public SearchSuggestion() {
        mSearchEntries = new ArrayList<>();
    }

    /*public void query(String query) {
        ServerInterface client = Client.getRetrofit().create(ServerInterface.class);
        Call<ArrayList<SearchEntry>> call = client.doSearchQuery(query);

        call.enqueue(new Callback<ArrayList<SearchEntry>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<SearchEntry>> call,
                                   @NonNull Response<ArrayList<SearchEntry>> response) {

                ArrayList<SearchEntry> entries = response.body();
                mSearchEntries.clear();

                if (entries != null)
                    mSearchEntries = entries;

                Log.d("SearchSuggestion.query",
                        String.format("Got %d results for '%s'", mSearchEntries.size(), query));
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<SearchEntry>> call, @NonNull Throwable t) {
                Log.d("SearchSuggestion.query",
                        "Failed to load Search results from the server: " + t.getMessage());
            }
        });
    }
*/
    public List<String> getNames() {
        List<String> names = new ArrayList<>();

        for (SearchEntry entry : mSearchEntries) {
            names.add(entry.getName() + "    " + entry.getExchange() + ":" + entry.getTicker());
        }

        return names;
    }
}

