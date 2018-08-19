package com.hixel.hixel;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.rule.ActivityTestRule;
import com.hixel.hixel.view.ui.DashboardActivity;
import org.junit.Rule;
import org.junit.Test;

public class DashboardUITest {

    @Rule
    public ActivityTestRule<DashboardActivity> mActivityRule = new ActivityTestRule<>(DashboardActivity.class);

    @Test
    public void correctTitleDisplayed() {
        onView(withId(R.id.toolbar_title)).check(matches(withText("Dashboard")));
    }

    @Test
    public void searchViewIsDisplayedAndClickable() {
        // User clicks the search icon
        onView(withId(R.id.action_search)).perform(click());

        // SearchView pops open and the user types a query
        onView(withId(android.support.design.R.id.search_src_text))
                .perform(typeText("Some text"))
                .check(matches(withText("Some text")));
    }

    @Test
    public void checkChartIsDisplayed() {
        onView(withId(R.id.chart)).check(matches(isDisplayed()));
    }

    @Test
    public void recyclerViewDisplaysCorrectNumberOfCompanies() throws InterruptedException {

        Thread.sleep(4000);

        onView(withId(R.id.recycler_view)).check(matches(hasChildCount(6)));

    }


    @Test
    public void spinnerIsVisibleWhenRecyclerViewIsNot() {}


}
