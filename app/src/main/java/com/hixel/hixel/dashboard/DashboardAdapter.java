package com.hixel.hixel.dashboard;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hixel.hixel.R;
import java.util.Locale;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {
    private final DashboardContract.Presenter presenter;

    DashboardAdapter(DashboardContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    @NonNull
    public DashboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardAdapter.ViewHolder holder, int position) {
        holder.companyName.setText(presenter.getCompanies().get(position).getName());
        holder.companyTicker.setText(presenter.getCompanies().get(position).getTicker());
        holder.companyHealth.setText(String.format(Locale.ENGLISH, "%.2f%%",
                presenter.getCompanies().get(position).getHealth()*100));
        holder.companyHealth.setTextColor(presenter.setHealthColor(position));
    }

    @Override
    public int getItemCount() {
        return presenter.getCompanies().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView companyName;
        TextView companyTicker;
        TextView companyHealth;

        ViewHolder(View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.company_name);
            companyTicker = itemView.findViewById(R.id.company_ticker);
            companyHealth = itemView.findViewById(R.id.company_health);
        }
    }

}
