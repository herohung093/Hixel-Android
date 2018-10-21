package com.hixel.hixel.ui.companycomparison;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import com.hixel.hixel.R;
import com.hixel.hixel.data.entities.Company;
import java.util.List;

public class CompanyScoreListAdapter
        extends RecyclerView.Adapter<CompanyScoreListAdapter.ViewHolder> {

    Context context;
    List<Company> companies;

    public CompanyScoreListAdapter(Context context, List<Company> companies) {
        this.context = context;
        this.companies = companies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.company_list_score_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String companyName = companies.get(position)
                .getName()
                .split("[\\s, ]")[0]
                .toLowerCase();

        companyName = companyName.substring(0, 1).toUpperCase() + companyName.substring(1);
        holder.companyName.setText(companyName);
        holder.progressBar.setCurrentProgress(calculateScore(companies.get(position)));
    }
    private int calculateScore(Company company){
        int score =((company.getHealthScore() + company.getReturnsScore() + company.getPerformanceScore() + company.getSafetyScore() + company.getStrengthScore())*4);
        return score;
    }
    @Override
    public int getItemCount() {
        return companies.size();
    }

    public void setCompanies(List<Company> companies){
        this.companies.addAll(companies);

        notifyDataSetChanged();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView companyName,score;
        private CircularProgressIndicator progressBar;

        ViewHolder(View itemView) {
            super(itemView);
            companyName= itemView.findViewById(R.id.textViewName41);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}