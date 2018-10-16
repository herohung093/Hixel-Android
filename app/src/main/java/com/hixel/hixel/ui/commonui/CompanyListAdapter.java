package com.hixel.hixel.ui.commonui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hixel.hixel.R;
import com.hixel.hixel.ui.companydetail.CompanyDetailActivity;
import com.hixel.hixel.data.entities.Company;

import com.hixel.hixel.databinding.RowBinding;
import java.util.List;

public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.ViewHolder> {

    private Context context;
    private List<Company> companies;

    public CompanyListAdapter(Context context, List<Company> companies) {
        this.context = context;
        this.companies = companies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RowBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.row, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        double companyScore = companies.get(position).getCurrentRatio();

        holder.binding.companyName.setText(companies.get(position).getFormattedName());
        holder.binding.companyTicker.setText(companies.get(position).getFormattedTicker());

        // Set the indicator based upon the current ratio
        if (companyScore < 1.0) {
            holder.binding.indicator
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.bad));
        } else if (companyScore >= 1.0 && companyScore <= 1.2) {
            holder.binding.indicator
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.average));
        } else {
            holder.binding.indicator
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.good));
        }

        holder.binding.foreground.setOnClickListener((View view) -> {
            String ticker = companies.get(position).getTicker();

            Intent intent = new Intent(context, CompanyDetailActivity.class);
            intent.putExtra("COMPANY_TICKER", ticker);
            intent.putExtra("FROM_PORTFOLIO", true);

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

    public void addItem(Company company) {
        companies.add(getItemCount(), company);
        notifyItemInserted(getItemCount());
    }

    public void setCompanies(List<Company> companies){
        this.companies = companies;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final RowBinding binding;

        ViewHolder(final RowBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }

        public ConstraintLayout getForeground() {
            return binding.foreground;
        }
    }
}
