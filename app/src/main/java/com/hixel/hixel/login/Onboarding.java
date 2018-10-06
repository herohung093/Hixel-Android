package com.hixel.hixel.login;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hixel.hixel.R;
import com.hixel.hixel.commonui.SliderAdapter;

public class Onboarding extends AppCompatActivity {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        ViewPager viewPager = findViewById(R.id.viewPager);
        linearLayout = findViewById(R.id.linearLayout);

        SliderAdapter sliderAdapter = new SliderAdapter(this);

        viewPager.setAdapter(sliderAdapter);
        viewPager.addOnPageChangeListener(viewListener);
    }

    public void addDotsIndicator(int position) {

        TextView[] dots = new TextView[3];
        linearLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(R.string.dot);
            dots[i].setTextSize(50);
            dots[i].setTextColor(getColor(R.color.primary_background));

            linearLayout.addView(dots[i]);
        }

        // TODO: Check if this if-statement can be removed
        if (dots.length > 0) {
            dots[position].setTextColor(getColor(R.color.good));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) { }
    };
}
