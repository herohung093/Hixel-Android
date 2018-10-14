package com.hixel.hixel.ui.commonui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import com.hixel.hixel.R;
import com.hixel.hixel.data.models.SearchEntry;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends ArrayAdapter<SearchEntry> {

    private Context context;
    private List<SearchEntry> searchEntries;

    public SearchAdapter(@NonNull Context context, List<SearchEntry> searchEntries) {
        super(context, 0, searchEntries);
        this.context = context;
        this.searchEntries = searchEntries;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.search_list_item, parent, false);

            TextView companyName = view.findViewById(R.id.company_name);
            companyName.setText(searchEntries.get(position).getName());

            TextView companyTicker = view.findViewById(R.id.company_ticker);
            String tickerFormat = searchEntries.get(position).getExchange() + ":" +
                    searchEntries.get(position).getTicker();

            companyTicker.setText(tickerFormat);
        }

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    String query = constraint.toString().toLowerCase();
                    List<SearchEntry> filtered = new ArrayList<>();

                    for (SearchEntry entry : searchEntries){
                        if (entry.getTicker().toLowerCase().contains(query)
                         || entry.getName().toLowerCase().contains(query)) {
                            filtered.add(entry);
                        }
                    }
                    // Now assign the values and count to the FilterResults object
                    filterResults.values = filtered;
                    filterResults.count = filtered.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
