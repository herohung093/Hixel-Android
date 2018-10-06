package com.hixel.hixel.commonui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hixel.hixel.R;
import com.hixel.hixel.companydetail.CompanyDetailActivity;
import com.hixel.hixel.data.models.Company;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    @SuppressWarnings("unused")
    private static final String TAG = DashboardAdapter.class.getSimpleName();

    private Context context;
    private List<Company> companies;

    public DashboardAdapter(Context context, List<Company> companies) {
        this.context = context;
        this.companies = companies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Company company = companies.get(position);
        double currentRatio = company.getRatio();

        String companyName = company.getFinancialIdentifiers().getName();

        companyName = companyName.substring(0, 1).toUpperCase() + companyName.substring(1);
        holder.companyName.setText(companyName);

        String tickerFormat = "NASDAQ:" + company.getFinancialIdentifiers().getTicker();
        holder.companyTicker.setText(tickerFormat);

        // TODO: Work out something with the indicator image
        // Set the indicator based upon the current ratio
        if (currentRatio < 1.0) {
            holder.indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.bad));
            // holder.companyIndicator.setBackgroundResource(R.drawable.ic_arrow_downward);
        } else if (currentRatio >= 1.0 && currentRatio <= 1.2) {
            holder.indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.average));
            // holder.companyIndicator.setBackgroundResource(R.drawable.ic_remove_black_24dp);
        } else {
            holder.indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.good));
            // holder.companyIndicator.setBackgroundResource(R.drawable.ic_arrow_upward_black_24dp);
        }


        holder.foreground.setOnClickListener((View view) -> {
            // TODO: Reimplement so that the Company view knows this was from the portfolio.
            Log.d(TAG, "" + company.getFinancialIdentifiers().getTicker());


            String ticker = company.getFinancialIdentifiers().getTicker();

            Intent intent = new Intent(context, CompanyDetailActivity.class);
            intent.putExtra("COMPANY_TICKER", ticker);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return companies.size();
    }

    public void removeItem(int position) {
        companies.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Company company, int position) {
        companies.add(position, company);
        notifyItemInserted(position);
    }

    public void addItems(List<Company> companies) {
        this.companies = companies;
        notifyDataSetChanged();
    }

    public void addItem(Company company) {
        companies.add(getItemCount(),company);
        notifyItemInserted(getItemCount());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout foreground;
        public ConstraintLayout background;
        public TextView companyTicker;
        public TextView companyName;
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
