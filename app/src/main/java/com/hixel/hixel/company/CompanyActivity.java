package com.hixel.hixel.company;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.hixel.hixel.R;
import com.hixel.hixel.databinding.ActivityDashboardBinding;
import com.hixel.hixel.models.Company;

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

        //NOTE: Had to remove these temporarily because of the refactored Company object.
        //Naveen will replace this logic for issue PTH-140, using presenter.getRatio(name, year)
        /*
            TextView liquidity = findViewById(R.id.liquidity_score);
            TextView leverage = findViewById(R.id.leverage_score);
            TextView health = findViewById(R.id.health_score);

            liquidity.setText(presenter.getLiquidity());
            liquidity.setTextColor(presenter.setHealthColor());
            leverage.setText(presenter.getLeverage());
            leverage.setTextColor(presenter.setLeverageColor());
            health.setText(presenter.getHealth());
            health.setTextColor(presenter.setHealthColor());
        */
    }

    public void setPresenter(@NonNull CompanyContract.Presenter presenter) {
        this.presenter = presenter;
    }

}
