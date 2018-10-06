package com.hixel.hixel.commonui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.hixel.hixel.R;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.companydetail.CompanyDetailActivity;
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

        holder.companyTicker.setText("NASDAQ: "+companies
                                              .get(position)
                                              .getIdentifiers()
                                              .getTicker());
        Company company = companies.get(position);
        int last_year = Calendar.getInstance().get(Calendar.YEAR) - 1;
        double currentRatio = company.getRatio("Current Ratio", last_year);


        if (currentRatio < 1.0) {
            holder.indicator.setBackgroundColor(ContextCompat.getColor(mContext, R.color.bad));
            holder.companyIndicator.setBackgroundResource(R.drawable.ic_arrow_downward);
        } else if (currentRatio >= 1.0 && currentRatio <= 1.2) {
            holder.indicator.setBackgroundColor(ContextCompat.getColor(mContext, R.color.average));
            holder.companyIndicator.setBackgroundResource(R.drawable.ic_remove_black_24dp);
        } else {
            holder.indicator.setBackgroundColor(ContextCompat.getColor(mContext, R.color.good));
            holder.companyIndicator.setBackgroundResource(R.drawable.ic_arrow_upward_black_24dp);
        }

        holder.foreground.setOnClickListener((View view) -> {
            Intent intent = new Intent(mContext, CompanyDetailActivity.class);
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
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout foreground;
        public ConstraintLayout background;
        TextView companyName;
        TextView companyTicker;
        ImageView companyIndicator;
        View indicator;

        ViewHolder(View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.company_name);
            companyTicker = itemView.findViewById(R.id.company_ticker);
            companyIndicator = itemView.findViewById(R.id.company_indicator);
            foreground = itemView.findViewById(R.id.foreground);
            background = itemView.findViewById(R.id.background);
            indicator = itemView.findViewById(R.id.indicator);
        }
    }
}
