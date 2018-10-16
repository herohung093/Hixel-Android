package com.hixel.hixel.ui.base;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import com.hixel.hixel.R;
import com.hixel.hixel.ui.companycomparison.CompanyComparisonActivity;
import com.hixel.hixel.ui.dashboard.DashboardActivity;
import com.hixel.hixel.ui.profile.ProfileActivity;

public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {

    protected T binding;
    protected BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void bindView(int layoutId) {
        binding = DataBindingUtil.setContentView(this, layoutId);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    public void setupBottomNavigationView(int currentItem) {
        bottomNavigationView.setSelectedItemId(currentItem);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_button:
                    startActivity(new Intent(this, DashboardActivity.class));
                case R.id.compare_button:
                    Intent moveToCompare = new Intent(this, CompanyComparisonActivity.class);
                    startActivity(moveToCompare);
                    break;
                case R.id.profile_button:
                    Intent moveToProfile = new Intent(this, ProfileActivity.class);
                    startActivity(moveToProfile);
                    break;
            }

            return true;
        });
    }
}
