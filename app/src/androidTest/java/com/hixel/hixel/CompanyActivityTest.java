package com.hixel.hixel;


import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.hixel.hixel.view.ui.CompanyActivity;

import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CompanyActivityTest {

    @Rule
    public ActivityTestRule<CompanyActivity> companyActivityTestRule =
            new ActivityTestRule<>(CompanyActivity.class);

}
