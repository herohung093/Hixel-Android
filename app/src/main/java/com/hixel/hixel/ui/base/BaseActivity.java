package com.hixel.hixel.ui.base;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.hixel.hixel.R;
import com.hixel.hixel.ui.companycomparison.CompanyComparisonActivity;
import com.hixel.hixel.ui.dashboard.DashboardActivity;
import com.hixel.hixel.ui.profile.ProfileActivity;

/**
 * Base activity that provides a Toolbar and BottomNavigationView
 *
 * @param <T> DataBinding for the Activity
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {

    protected T binding;
    protected BottomNavigationView bottomNavigationView;
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    public void bindView(int layoutId) {
        binding = DataBindingUtil.setContentView(this, layoutId);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    public void setupToolbar(int title, boolean enableBackButton, boolean enableSearchWidget) {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

    }

    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    /**
     * Controls the logic for the BottomNavigationView.
     * @param currentItem The item to be highlighted in the Menu.
     */
    public void setupBottomNavigationView(int currentItem) {
        bottomNavigationView.setSelectedItemId(currentItem);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_button:
                    startActivity(new Intent(this, DashboardActivity.class));
                    break;
                case R.id.compare_button:
                    Intent moveToCompare = new Intent(this, CompanyComparisonActivity.class);
                    startActivity(moveToCompare);
                    break;
                case R.id.profile_button:
                    Intent moveToProfile = new Intent(this, ProfileActivity.class);
                    startActivity(moveToProfile);
                    break;
                default:
                    break;
            }

            return true;
        });
    }
}
