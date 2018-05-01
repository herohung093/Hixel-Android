package com.hixel.hixel;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import com.hixel.hixel.dashboard.Dashboard;
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
public class DashboardTest {

    @Rule
    public ActivityTestRule<Dashboard> dashboardActivityTestRule =
            new ActivityTestRule<>(Dashboard.class);

    @Test
    public void toolbarDisplaysDashboardAsTitle() {
        onView(withId(R.id.toolbar_title))
                .check(matches(withText(containsString("Dashboard"))));
    }

}
