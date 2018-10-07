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
import com.hixel.hixel.companydetail.CompanyDetailActivity;
import com.hixel.hixel.data.entities.Company;
import java.util.List;

public class ComparisonAdapter extends RecyclerView.Adapter<ComparisonAdapter.ViewHolder> {

    private List<Company> companies;
    private Context context;

    public ComparisonAdapter(Context context, List<Company> companies) {
        this.companies = companies;
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

        String companyTicker = companies.get(position).getFormattedTicker();
        String companyName = companies.get(position).getFormattedName();

        holder.companyName.setText(companyName);
        holder.companyTicker.setText(companyTicker);

        double currentRatio = companies.get(position).getCurrentRatio();


        if (currentRatio < 1.0) {
            holder.indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.bad));
            holder.companyIndicator.setBackgroundResource(R.drawable.ic_arrow_downward);
        } else if (currentRatio >= 1.0 && currentRatio <= 1.2) {
            holder.indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.average));
            holder.companyIndicator.setBackgroundResource(R.drawable.ic_remove_black_24dp);
        } else {
            holder.indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.good));
            holder.companyIndicator.setBackgroundResource(R.drawable.ic_arrow_upward_black_24dp);
        }

        holder.foreground.setOnClickListener((View view) -> {
            Intent intent = new Intent(context, CompanyDetailActivity.class);
            // intent.putExtra("CURRENT_COMPANY", companies.get(holder.getAdapterPosition()));
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return (companies != null) ? companies.size() : 0;
    }

    public void removeItem(int position) {
        companies.remove(position);
        notifyItemRemoved(position);
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
