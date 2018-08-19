package com.hixel.hixel.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hixel.hixel.R;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.view.ui.CompanyActivity;
import java.util.Calendar;
import java.util.List;

public class ComparisonAdapter extends RecyclerView.Adapter<ComparisonAdapter.ViewHolder> {

    private Context mContext;
    private List<Company> companies;

    public ComparisonAdapter(Context mContext, List<Company> companies) {
        this.mContext = mContext;
        this.companies = companies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        String companyName = companies
            .get(position)
            .getIdentifiers()
            .getName()
            .split("\\,| ")[0]
            .toLowerCase();

        companyName = companyName.substring(0, 1).toUpperCase() + companyName.substring(1);
        holder.companyName.setText(companyName);

        holder.companyTicker.setText("NASDAQ:"+companies
                                              .get(position)
                                              .getIdentifiers()
                                              .getTicker());

        int last_year = Calendar.getInstance().get(Calendar.YEAR) - 1;

        /*holder.companyHealth.setText(String.format(Locale.ENGLISH, "%.1f%%",
                                    companies
                                                   .get(position)
                                                   .getRatio("Current Ratio", last_year) * 100));*/

        holder.foreground.setOnClickListener((View view) -> {
            Intent intent = new Intent(mContext, CompanyActivity.class);
            intent.putExtra("CURRENT_COMPANY",
                    companies.get(holder.getAdapterPosition()));

            mContext.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        if(companies!=null)
         return companies.size();
        else return 0;

    }

    public void removeItem(int position) {
        companies.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Company company, int position) {
        companies.add(position, company);
        notifyItemInserted(position);
    }

    public void addItem(Company company) {
        this.companies.add(company);
        notifyDataSetChanged();
    }
    public void setCompanies(List<Company> companies){
        this.companies=companies;
        notifyDataSetChanged();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout foreground;
        public ConstraintLayout background;
        TextView companyName;
        TextView companyTicker;
        TextView companyHealth;

        ViewHolder(View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.company_name);
            companyTicker = itemView.findViewById(R.id.company_ticker);
            // companyHealth = itemView.findViewById(R.id.indicator_value);
            foreground = itemView.findViewById(R.id.foreground);
            background = itemView.findViewById(R.id.background);
        }
    }
}
