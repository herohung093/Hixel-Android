package com.hixel.hixel.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import com.chyrta.onboarder.OnboarderActivity;
import com.chyrta.onboarder.OnboarderPage;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;
import com.hixel.hixel.R;

import java.util.ArrayList;
import java.util.List;

public class Onboarding extends OnboarderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Integer> colorList = new ArrayList<>();
        colorList.add(R.color.onboarding_green);
        colorList.add(R.color.onboarding_blue);
        colorList.add(R.color.onboarding_alt_orange);
        // setColorBackground(colorList);

        // Available properties
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");
        setFont(face);

        // Hide navigation controls
        showNavigationControls(false);

        // Set pager indicator colors
        // setInactiveIndicatorColor(R.color.grey);
        // setActiveIndicatorColor(R.color.white);

        // Set finish button text
        setFinishButtonTitle("Get Started");

        // Set the finish button style
        setFinishButtonDrawableStyle(ContextCompat.getDrawable(this, R.drawable.onboarding_button_style));

        setOnboardPages(getOnBoardingCards());
    }

    @Override
    public void onFinishButtonPressed() {
        Intent moveToSignup = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(moveToSignup);
    }

    public List<OnboarderPage> getOnBoardingCards() {

        List<AhoyOnboarderCard> cards = new ArrayList<>();
        cards.add(new AhoyOnboarderCard(
                "No crazy analysis!",
                "We use easy to understand charts, and assign a company a score out of 100.",
                R.drawable.onboarding_alternate_barchart)
        );

        cards.add(new AhoyOnboarderCard(
                "Companies you care about.",
                "Add and remove companies as you like, and compare them to see whose best.",
                R.drawable.onboarding_compare)
        );

        cards.add(new AhoyOnboarderCard(
                "Let's Go!",
                "Sign up now and make your own portfolio in minutes!",
                R.drawable.onboarding_launch)
        );

        /*
            card.setTitleTextSize(dpToPixels(10, this));
            card1.setDescriptionTextSize(dpToPixels(8, this));
            card1.setBackgroundColor(R.color.black_transparent);
            card1.setTitleColor(R.color.white);
            card1.setDescriptionColor(R.color.grey_200);
            card1.setIconLayoutParams(iconWidth, iconHeight, marginTop, marginLeft, marginRight, marginBottom);
         */

        return cards;
    }
}
