package com.hixel.hixel.comparison;

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
import com.hixel.hixel.company.CompanyActivity;

import java.util.Calendar;
import java.util.Locale;

public class ComparisonAdapter
        extends RecyclerView.Adapter<ComparisonAdapter.ViewHolder>{

    private final ComparisonContract.Presenter presenter;
    private Context mContext;

    ComparisonAdapter(Context context, ComparisonContract.Presenter presenter) {
        this.presenter = presenter;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        String companyName = presenter.getListCompareCompanies()
            .get(position)
            .getIdentifiers()
            .getName()
            .split("\\,| ")[0]
            .toLowerCase();
        holder.companyName.setText(companyName.substring(0, 1).toUpperCase() + companyName.substring(1));

        holder.companyTicker.setText(presenter.getListCompareCompanies()
                                              .get(position)
                                              .getIdentifiers()
                                              .getTicker());

        int last_year = Calendar.getInstance().get(Calendar.YEAR) - 1;

        holder.companyHealth.setText(String.format(Locale.ENGLISH, "%.1f%%",
                                    presenter.getListCompareCompanies()
                                                   .get(position)
                                                   .getRatio("Return-on-Equity Ratio", last_year) * 100));

        //TODO: Replace as part of PTH-140
        //holder.companyHealth.setTextColor(presenter.setHealthColor(position));

        holder.parentLayout.setOnClickListener((View view) -> {
            Intent intent = new Intent(mContext, CompanyActivity.class);
            intent.putExtra("company",
                    presenter.getListCompareCompanies().get(holder.getAdapterPosition()));

            mContext.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return presenter.getListCompareCompanies().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // Adding this for the OnClick
        ConstraintLayout parentLayout;
        TextView companyName;
        TextView companyTicker;
        TextView companyHealth;

        ViewHolder(View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.company_name);
            companyTicker = itemView.findViewById(R.id.company_ticker);
            companyHealth = itemView.findViewById(R.id.indicator_value);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
