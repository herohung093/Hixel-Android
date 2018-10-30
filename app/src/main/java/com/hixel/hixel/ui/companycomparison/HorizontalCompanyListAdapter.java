package com.hixel.hixel.ui.companycomparison;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hixel.hixel.R;
import com.hixel.hixel.data.entities.company.Company;
import com.hixel.hixel.ui.commonui.HorizontalCompanyListOnClickListener;
import java.util.List;

public class HorizontalCompanyListAdapter extends RecyclerView.Adapter<HorizontalCompanyListAdapter.ViewHolder>{

    private List<Company> companies;
    private final HorizontalCompanyListOnClickListener listener;

    HorizontalCompanyListAdapter(List<Company> companies, HorizontalCompanyListOnClickListener listener) {
        this.companies = companies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HorizontalCompanyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.company_list_horizontal_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String companyName = companies.get(position).getIdentifiers().getFormattedName();
        companyName = companyName.substring(0, 1).toUpperCase() + companyName.substring(1);
        holder.companyNameTV.setText(companyName);
        holder.cardView.setOnClickListener(view -> listener.onClick(companies.get(position)));
    }


    @Override
    public int getItemCount() {
        return companies.size();
    }

    public void setCompanies(List<Company> companies){
        this.companies=companies;
        notifyDataSetChanged();
    }

    public void addItems(List<Company> companies) {
        this.companies = companies;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        companies.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItem(String ticker){
        int index=-1;
        for (Company c: companies){
            if(c.getIdentifiers().getTicker().equalsIgnoreCase(ticker)){
                index = companies.indexOf(c);
            }
        }
        if(index!=-1){
            companies.remove(index);
            notifyDataSetChanged();
        }

    }

    public Company getCompany(int position) {
        return companies.get(position);
    }

    public void addItem(Company company) {
        companies.add(getItemCount(), company);
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView companyNameTV;
        public final CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            companyNameTV= itemView.findViewById(R.id.companyName_tv);
            cardView= itemView.findViewById(R.id.company_item_cardview);
        }

        public Company getSelectedCompany(){
            return companies.get(getAdapterPosition());
        }
    }
}
