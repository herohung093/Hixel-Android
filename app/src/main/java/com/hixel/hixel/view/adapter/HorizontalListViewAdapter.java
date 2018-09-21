package com.hixel.hixel.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hixel.hixel.R;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.view.ui.GraphFragment;
import java.util.ArrayList;

public class HorizontalListViewAdapter extends RecyclerView.Adapter<HorizontalListViewAdapter.ViewHolder> implements View.OnClickListener {

    ArrayList<String> ratios;
    ArrayList<Company> companies;
    GraphFragment fragmentGraph;
    static Context context;
    int rowIndex =0;
    boolean[] items= new boolean[5];
    private static SparseBooleanArray sSelectedItems;
    public HorizontalListViewAdapter(Context context,ArrayList<String> ratios,
        ArrayList<Company> companies, GraphFragment fragmentGraph) {
        this.context=context;
        this.ratios = ratios;
        this.companies = companies;
        this.fragmentGraph = fragmentGraph;
        sSelectedItems = new SparseBooleanArray();
        sSelectedItems.append(0,true);
    }

    @NonNull
    @Override
    public HorizontalListViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
        int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ratios_list_horizontal_layout, parent, false);

        return new HorizontalListViewAdapter.ViewHolder(view);
    }
    private void setRowIndex(int index){
        rowIndex= index;
    }
    @Override
    public void onBindViewHolder(@NonNull HorizontalListViewAdapter.ViewHolder holder,
        int position) {
        holder.setSelectedItem(holder.getAdapterPosition()==rowIndex);
        holder.tvSpecies.setText(ratios.get(position));

        holder.tvSpecies.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                fragmentGraph.drawGraph(companies,ratios.get(position));
                //setRowIndex(position);
                notifyItemChanged(rowIndex);
                rowIndex=position;
                notifyItemChanged(rowIndex);

            }
        });

    }

    @Override
    public int getItemCount() {
        return ratios.size();
    }

    @Override
    public void onClick(View view) {

    }
    public void selectedItemController(int position){

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSpecies;
        public CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            tvSpecies =  itemView.findViewById(R.id.tv_species);
            cardView =  itemView.findViewById(R.id.ratio_item_cardView);
        }

        public void setSelectedItem(boolean selected){
            if(selected)
                cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
            else
                cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }
    }

}
