package com.hixel.hixel;


import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.hixel.hixel.companydetail.CompanyDetailActivity;

import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CompanyDetailActivityTest {

    @Rule
    public ActivityTestRule<CompanyDetailActivity> companyActivityTestRule =
            new ActivityTestRule<>(CompanyDetailActivity.class);

}
