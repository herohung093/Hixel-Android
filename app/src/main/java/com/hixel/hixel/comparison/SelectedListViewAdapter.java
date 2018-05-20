package com.hixel.hixel.comparison;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hixel.hixel.R;
import com.hixel.hixel.company.CompanyIdentifiers;

import java.util.ArrayList;

class SelectedListViewAdapter extends ArrayAdapter<CompanyIdentifiers> {
    private final Context context;
    private final ComparisonContract.Presenter mPresenter;



    public SelectedListViewAdapter( Context context, ComparisonContract.Presenter mPresenter) {
        super(context, -1, (ArrayList)mPresenter.getListCompareCompanies());
        this.context = context;
        this.mPresenter=mPresenter;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView= inflater.inflate(R.layout.selected_company_row,parent,false);
        TextView companyName = (TextView) rowView.findViewById(R.id.companyName);
        TextView companyTicker= (TextView) rowView.findViewById(R.id.companyTicker);
        companyName.setText(mPresenter.getListCompareCompanies().get(position).getIdentifiers().getName());
        companyTicker.setText(mPresenter.getListCompareCompanies().get(position).getIdentifiers().getTicker());

        return rowView;
    }
    public void removeAt(int position)
    {
        mPresenter.getListCompareCompanies().remove(position);
    }
}
