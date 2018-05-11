package com.hixel.hixel.company;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.hixel.hixel.R;
import com.hixel.hixel.models.Company;
import com.hixel.hixel.databinding.ActivityDashboardBinding;

public class CompanyActivity extends AppCompatActivity implements CompanyContract.View {

    private CompanyContract.Presenter presenter;
    private ActivityDashboardBinding binding;
    private String TAG = "COMPANY_VIEW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        presenter = new CompanyPresenter(this);
        presenter.setCompany((Company) getIntent().getSerializableExtra("company"));
        presenter.start();


        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(presenter.getCompanyName());

        TextView liquidity = findViewById(R.id.liquidity_score);
        TextView leverage = findViewById(R.id.leverage_score);
        TextView health = findViewById(R.id.health_score);

        liquidity.setText(presenter.getLiquidity());
        liquidity.setTextColor(presenter.setHealthColor());
        leverage.setText(presenter.getLeverage());
        leverage.setTextColor(presenter.setLeverageColor());
        health.setText(presenter.getHealth());
        health.setTextColor(presenter.setHealthColor());
    }

    public void setPresenter(@NonNull CompanyContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
