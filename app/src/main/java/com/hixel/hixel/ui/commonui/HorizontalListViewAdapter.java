package com.hixel.hixel.ui.commonui;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hixel.hixel.R;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.ui.GraphInterface;
import java.util.List;

public class HorizontalListViewAdapter extends RecyclerView.Adapter<HorizontalListViewAdapter.ViewHolder> {

    private List<Company> companies;
    private GraphInterface fragmentGraph;
    private Context context;
    private int rowIndex = 0;

    private HorizontalListViewOnClickListener listener;

    // TODO: XML or Const file?
    private static final String[] ratios = {"Returns", "Performance", "Strength", "Health", "Safety"};

    public HorizontalListViewAdapter(Context context, List<Company> companies, GraphInterface fragmentGraph, HorizontalListViewOnClickListener listener) {
        this.context = context;
        this.companies = companies;
        this.fragmentGraph = fragmentGraph;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HorizontalListViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ratios_list_horizontal_layout, parent, false);

        return new HorizontalListViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalListViewAdapter.ViewHolder holder, int position) {
        /*
        holder.setSelectedItem(holder.getAdapterPosition()==rowIndex);
        holder.tvSpecies.setText(ratios.get(position));

        holder.tvSpecies.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if(companies.size() > 1){
                    fragmentGraph.drawGraph(companies,ratios.get(position));
                } else {
                    fragmentGraph.drawGraph(companies,ratios.get(position));
                }

                // setRowIndex(position);
                notifyItemChanged(rowIndex);
                rowIndex=position;
                notifyItemChanged(rowIndex);
            }
        });*/


        if (position == rowIndex) {
            holder.cardView.setCardBackgroundColor(
                    context.getResources().getColor(R.color.colorPrimaryDark));
        } else {
            holder.cardView.setCardBackgroundColor(
                    context.getResources().getColor(R.color.colorPrimary));
        }

        holder.tvSpecies.setText(ratios[position]);

        // TODO: This is messy.
        holder.tvSpecies.setOnClickListener(view -> {
            //fragmentGraph.drawGraph(companies,ratios.get(position));
            notifyItemChanged(rowIndex);
            rowIndex = position;
            notifyItemChanged(rowIndex);

            listener.onClick(ratios[position]);
        });
    }

    @Override
    public int getItemCount() {
        return ratios.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder  {
        public CardView cardView;
        TextView tvSpecies;

        ViewHolder(View itemView) {
            super(itemView);
            tvSpecies =  itemView.findViewById(R.id.tv_species);
            cardView =  itemView.findViewById(R.id.ratio_item_cardView);
        }

        public void setSelectedItem(boolean selected) {
            if(selected)
                // Color primary dark
                cardView.setCardBackgroundColor(Color.parseColor("#303F9F"));
            else
                // Color primary
                cardView.setCardBackgroundColor(Color.parseColor("#172B4D"));
        }
    }
}
