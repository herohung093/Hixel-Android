package com.hixel.hixel.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hixel.hixel.R;
import com.hixel.hixel.company.CompanyActivity;
import com.hixel.hixel.models.Company;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private final DashboardContract.Presenter presenter;
    private Context context;

    DashboardAdapter(Context context, DashboardContract.Presenter presenter) {
        this.presenter = presenter;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        // TODO: fix regex.
        String companyName = presenter.getCompanies()
                .get(position)
                .getIdentifiers()
                .getName()
                .split("\\,| ")[0]
                .toLowerCase();

        companyName = companyName.substring(0, 1).toUpperCase() + companyName.substring(1);

        holder.companyName.setText(companyName);

        // NOTE: This is a temporary fix!!!
        // Need to get ticker exchange from server.
        String tickerFormat = "NASDAQ:" + presenter.getCompanies()
                                                    .get(position)
                                                    .getIdentifiers()
                                                    .getTicker();

        holder.companyTicker.setText(tickerFormat);


        int last_year = Calendar.getInstance().get(Calendar.YEAR) - 1;

        // TODO: Replace with an 'indicator arrow'
        holder.companyIndicator.setText(String.format(Locale.ENGLISH, "%.1f%%",
                                    presenter.getCompanies()
                                                   .get(position)
                                                   .getRatio("Return-on-Equity Ratio", last_year) * 100));

        holder.foreground.setOnClickListener((View view) -> {
            Intent intent = new Intent(context, CompanyActivity.class);

            Bundle extras = new Bundle();

            ArrayList<Company> companies = new ArrayList<>(presenter.getCompanies());

            extras.putSerializable("CURRENT_COMPANY", presenter.getCompanies().get(position));
            extras.putSerializable("PORTFOLIO", companies);

            intent.putExtras(extras);

            context.startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return presenter.getCompanies().size();
    }

    public void removeItem(int position) {
        presenter.getCompanies().remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Company company, int position) {
        presenter.getCompanies().add(position, company);
        notifyItemInserted(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout foreground;
        ConstraintLayout background;
        TextView companyName;
        TextView companyTicker;
        TextView companyIndicator;

        ViewHolder(View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.company_name);
            companyTicker = itemView.findViewById(R.id.company_ticker);
            companyIndicator = itemView.findViewById(R.id.indicator_value);
            foreground = itemView.findViewById(R.id.foreground);
            background = itemView.findViewById(R.id.background);
        }

    }
    public void addItem(Company company)
    {
        presenter.getCompanies().add(getItemCount(),company);
        notifyItemInserted(getItemCount());// re check here

    }
}
