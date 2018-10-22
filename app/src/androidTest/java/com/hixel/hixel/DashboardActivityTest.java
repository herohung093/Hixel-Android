package com.hixel.hixel;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import com.hixel.hixel.ui.dashboard.DashboardActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import android.support.test.rule.ActivityTestRule;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class DashboardActivityTest {

    @Rule
    public ActivityTestRule<DashboardActivity> dashboardActivityTestRule =
            new ActivityTestRule<>(DashboardActivity.class);

    @Test
    public void toolbarDisplaysDashboardAsTitle() {
        onView(withId(R.id.toolbar))
                .check(matches(withText(containsString("Dashboard"))));
    }

    @Test
    public void chartIsBeingDisplayed() {
        onView(withId(R.id.main_graph));
    }

    @Test
    public void testSpinnerChangesArrayAdapter() {

    }

}
