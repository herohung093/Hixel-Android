package com.hixel.hixel.commonui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hixel.hixel.R;
import com.hixel.hixel.ui.companycomparison.GraphFragment;
import com.hixel.hixel.data.entities.Company;
import java.util.ArrayList;

public class HorizontalListViewAdapter extends RecyclerView.Adapter<HorizontalListViewAdapter.ViewHolder> {

    private ArrayList<String> ratios;
    private ArrayList<Company> companies;
    private GraphFragment fragmentGraph;
    private Context context;
    private int rowIndex = 0;

    public HorizontalListViewAdapter(Context context,ArrayList<String> ratios, ArrayList<Company> companies, GraphFragment fragmentGraph) {
        this.context = context;
        this.ratios = ratios;
        this.companies = companies;
        this.fragmentGraph = fragmentGraph;
    }

    @NonNull
    @Override
    public HorizontalListViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
        int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ratios_list_horizontal_layout, parent, false);

        return new HorizontalListViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalListViewAdapter.ViewHolder holder, int position) {

        if (position == rowIndex) {
            holder.cardView.setCardBackgroundColor(
                    context.getResources().getColor(R.color.colorPrimaryDark));
        } else {
            holder.cardView.setCardBackgroundColor(
                    context.getResources().getColor(R.color.colorPrimary));
        }

        holder.tvSpecies.setText(ratios.get(position));

        holder.tvSpecies.setOnClickListener(view -> {
            fragmentGraph.drawGraph(companies,ratios.get(position));
            notifyItemChanged(rowIndex);
            rowIndex = position;
            notifyItemChanged(rowIndex);
        });
    }

    @Override
    public int getItemCount() {
        return ratios.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        TextView tvSpecies;

        ViewHolder(View itemView) {
            super(itemView);
            tvSpecies =  itemView.findViewById(R.id.tv_species);
            cardView =  itemView.findViewById(R.id.ratio_item_cardView);
        }
    }
}
