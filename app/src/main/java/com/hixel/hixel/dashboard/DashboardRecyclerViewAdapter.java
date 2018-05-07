package com.hixel.hixel.dashboard;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hixel.hixel.R;
import com.hixel.hixel.company.CompanyActivity;
import java.util.Locale;

public class DashboardRecyclerViewAdapter
        extends RecyclerView.Adapter<DashboardRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "DashboardRecyclerView";
    private final DashboardContract.Presenter presenter;
    private Context mContext;

    DashboardRecyclerViewAdapter(Context context, DashboardContract.Presenter presenter) {
        this.presenter = presenter;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.companyName.setText(presenter.getCompanies().get(position).getName());
        holder.companyTicker.setText(presenter.getCompanies().get(position).getTicker());
        holder.companyHealth.setText(String.format(Locale.ENGLISH, "%.2f%%",
                presenter.getCompanies().get(position).getHealth()*100));
        holder.companyHealth.setTextColor(presenter.setHealthColor(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CompanyActivity.class);
                intent.putExtra("company",
                        presenter.getCompanies().get(holder.getAdapterPosition()));

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return presenter.getCompanies().size();
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
            companyHealth = itemView.findViewById(R.id.company_health);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
