package com.hixel.hixel;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import android.support.test.rule.ActivityTestRule;
import com.hixel.hixel.view.ui.LoginActivity;
import org.junit.Rule;
import org.junit.Test;
public class LoginUITest {

    @Rule public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void textViewClick_goToForgotPasswordActivity(){
        onView(withId(R.id.link_forgot_password)).perform(click());
        onView(withId(R.id.forgot_pass_root_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void textViewClick_goToSignUpActivity(){
        onView(withId(R.id.link_signup)).perform(click());
        onView(withId(R.id.signup_root_layout)).check(matches(isDisplayed()));
    }
    @Test
    public void buttonClick_goToDashboardpActivity(){
        onView(withId(R.id.emailWrapper)).perform(typeText("hung@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordWrapper)).perform(typeText("1234"),closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        onView(withId(R.id.dashboard_root_layout)).check(matches(isDisplayed()));
    }
}
