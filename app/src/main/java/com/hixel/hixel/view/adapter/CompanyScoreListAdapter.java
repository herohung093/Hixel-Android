package com.hixel.hixel.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import com.hixel.hixel.R;
import com.hixel.hixel.service.models.Company;
import java.util.List;
import java.util.Random;

public class CompanyScoreListAdapter extends RecyclerView.Adapter<CompanyScoreListAdapter.ViewHolder>{

    Context context;
    List<Company> companies;

    public CompanyScoreListAdapter(Context context,
        List<Company> companies) {
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
        String companyName = companies.get(position).getIdentifiers()
            .getName()
            .split("[\\s, ]")[0]
            .toLowerCase();

        companyName = companyName.substring(0, 1).toUpperCase() + companyName.substring(1);
        holder.companyName.setText(companyName);
        Random rand = new Random();
        holder.progressBar.setCurrentProgress(rand.nextInt(100)+1);
    }

    @Override
    public int getItemCount() {
        return companies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView companyName,score;
        private CircularProgressIndicator progressBar;
        public ViewHolder(View itemView) {
            super(itemView);
            companyName= itemView.findViewById(R.id.textViewName41);
            progressBar = itemView.findViewById(R.id.progressBar);
            //score= itemView.findViewById(R.id.textView41);
        }
    }
}
