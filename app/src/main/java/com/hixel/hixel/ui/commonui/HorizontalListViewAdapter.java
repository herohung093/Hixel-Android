package com.hixel.hixel.ui.commonui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hixel.hixel.R;

/**
 * Displays a list of companies and their 'score' horizontally
 */
public class HorizontalListViewAdapter
        extends RecyclerView.Adapter<HorizontalListViewAdapter.ViewHolder> {

    private final Context context;
    private int rowIndex = 0;

    private final HorizontalListViewOnClickListener listener;

    private static final String[] ratios =
            {"Returns", "Performance", "Strength", "Health", "Safety"};

    public HorizontalListViewAdapter(Context context, HorizontalListViewOnClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HorizontalListViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
            int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ratios_list_horizontal_layout, parent, false);

        return new HorizontalListViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalListViewAdapter.ViewHolder holder,
            int position) {
        if (position == rowIndex) {
            holder.cardView.setCardBackgroundColor(
                    context.getResources().getColor(R.color.colorPrimaryDark));
        } else {
            holder.cardView.setCardBackgroundColor(
                    context.getResources().getColor(R.color.colorPrimary));
        }

        holder.tvSpecies.setText(ratios[position]);
        holder.tvSpecies.setOnClickListener(view -> {
            // fragmentGraph.drawGraph(companies,ratios.get(position));
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
        public final CardView cardView;
        final TextView tvSpecies;

        ViewHolder(View itemView) {
            super(itemView);
            tvSpecies =  itemView.findViewById(R.id.tv_species);
            cardView =  itemView.findViewById(R.id.ratio_item_cardView);
        }
    }
}
