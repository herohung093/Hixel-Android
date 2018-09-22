package com.hixel.hixel.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hixel.hixel.R;
import com.hixel.hixel.service.models.Company;
import java.util.List;

public class HorizontalCompanyListAdapter extends RecyclerView.Adapter<HorizontalCompanyListAdapter.ViewHolder>{

    private Context context;
    private List<Company> companies;

    public HorizontalCompanyListAdapter(Context context, List<Company> companies) {
        this.context = context;
        this.companies = companies;
    }

    @NonNull
    @Override
    public HorizontalCompanyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).
            inflate(R.layout.company_list_horizontal_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String companyName = companies.get(position).getIdentifiers()
            .getName()
            .split("[\\s, ]")[0]
            .toLowerCase();

        companyName = companyName.substring(0, 1).toUpperCase() + companyName.substring(1);
        holder.companyNameTV.setText(companyName);
    }
    public void removeItem(int position) {
        companies.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public int getItemCount() {
        return companies.size();
    }
    public void setCompanies(List<Company> companies){
        this.companies=companies;
        notifyDataSetChanged();
    }
    public void addItem(Company company) {
        companies.add(getItemCount(),company);
        notifyItemInserted(getItemCount());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView companyNameTV;
        public CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            companyNameTV= itemView.findViewById(R.id.companyName_tv);
            cardView= itemView.findViewById(R.id.company_item_cardview);
        }
        public Company getSelectedCompany(){
            return companies.get(getAdapterPosition());
        }
    }
}
