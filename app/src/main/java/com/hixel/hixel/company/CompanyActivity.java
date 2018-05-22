package com.hixel.hixel.company;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.hixel.hixel.R;
import com.hixel.hixel.models.Company;

import java.util.ArrayList;

public class CompanyActivity extends AppCompatActivity implements CompanyContract.View {

    private CompanyContract.Presenter presenter;
    private String TAG = "COMPANY_VIEW";
    private ArrayList<String> ratios1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        // doMeta();
        presenter = new CompanyPresenter(this);
        presenter.setCompany((Company) getIntent().getSerializableExtra("company"));
        presenter.start();


        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(presenter.getCompanyName());
    }

    public void updateRatios(ArrayList<String> ratios1) {
        TextView liquidity = findViewById(R.id.liquidity_text);
        TextView leverage = findViewById(R.id.leverage_text);
        TextView health = findViewById(R.id.health_text);
        liquidity.setText(ratios1.get(0));
        leverage.setText(ratios1.get(1));
        health.setText(ratios1.get(2));

        TextView liquidityScore = findViewById(R.id.liquidity_score);
        TextView leverageScore = findViewById(R.id.leverage_score);
        TextView healthScore = findViewById(R.id.health_score);
        liquidityScore.setText(getValue(ratios1.get(0), 2017));
        leverageScore.setText(getValue(ratios1.get(1), 2017));
        healthScore.setText(getValue(ratios1.get(2), 2017));


    }

    public void setPresenter(@NonNull CompanyContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public String getValue(String name,int year)
    {
       String value= presenter.getRatio(name,year);
       if(value.length()>4)
       {
           return value.substring(0,5);
       }
       else
       {
           return value;
       }
    }



}
