package com.hixel.hixel.commonui;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hixel.hixel.R;
import com.hixel.hixel.data.models.Company;
import java.util.List;

public class HorizontalCompanyListAdapter extends RecyclerView.Adapter<HorizontalCompanyListAdapter.ViewHolder>{

    private static final String TAG = HorizontalCompanyListAdapter.class.getSimpleName();


    private List<Company> companies;

    public HorizontalCompanyListAdapter(List<Company> companies) {
        this.companies = companies;
    }

    @NonNull
    @Override
    public HorizontalCompanyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.company_list_horizontal_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String companyName = companies.get(position)
                                      .getFinancialIdentifiers()
                                      .getName()
                                      .split("[\\s, ]")[0]
                                      .toLowerCase();

        Log.d(TAG, "onBindViewHolder: " + companyName);

        companyName = companyName.substring(0, 1).toUpperCase() + companyName.substring(1);
        holder.companyNameTV.setText(companyName);
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView companyNameTV;
        public CardView cardView;

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
