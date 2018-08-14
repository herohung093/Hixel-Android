package com.hixel.hixel.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hixel.hixel.R;
import com.hixel.hixel.view.ui.CompanyActivity;
import com.hixel.hixel.service.models.Company;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

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
        double currentRatio = company.getRatio("Current Ratio", 2017);

        // TODO: fix regex.
        String companyName = company.getIdentifiers()
                .getName()
                .split("\\,| ")[0]
                .toLowerCase();

        companyName = companyName.substring(0, 1).toUpperCase() + companyName.substring(1);

        holder.companyName.setText(companyName);

        String tickerFormat = "NASDAQ:" + company.getIdentifiers().getTicker();

        holder.companyTicker.setText(tickerFormat);

        holder.companyIndicator.setBackgroundResource(R.drawable.ic_arrow_downward);

        if (currentRatio < 1.0) {
            holder.companyIndicator.setBackgroundResource(R.drawable.ic_arrow_upward_black_24dp);
        } else if (currentRatio < 0.0) {
            holder.companyIndicator.setBackgroundResource(R.drawable.ic_remove_black_24dp);
        }


        holder.foreground.setOnClickListener((View view) -> {
            Intent intent = new Intent(context, CompanyActivity.class);
            Bundle extras = new Bundle();

            extras.putSerializable("CURRENT_COMPANY", company);
            extras.putSerializable("PORTFOLIO", (ArrayList) companies);

            intent.putExtras(extras);

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout foreground;
        public ConstraintLayout background;
        public TextView companyTicker;
        public TextView companyName;
        public ImageView companyIndicator;

        ViewHolder(View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.company_name);
            companyTicker = itemView.findViewById(R.id.company_ticker);
            companyIndicator = itemView.findViewById(R.id.company_indicator);
            foreground = itemView.findViewById(R.id.foreground);
            background = itemView.findViewById(R.id.background);
        }
    }

    public void addItem(Company company) {
        companies.add(getItemCount(),company);
        notifyItemInserted(getItemCount());
    }
}
