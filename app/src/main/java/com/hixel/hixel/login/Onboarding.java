package com.hixel.hixel.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;
import com.hixel.hixel.R;

import java.util.ArrayList;
import java.util.List;

public class Onboarding extends AhoyOnboarderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Integer> colorList = new ArrayList<>();
        colorList.add(R.color.onboarding_green);
        colorList.add(R.color.onboarding_blue);
        colorList.add(R.color.onboarding_yellow);
        setColorBackground(colorList);

        // Available properties
        // Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        // setFont(face);

        // Set pager indicator colors
        // setInactiveIndicatorColor(R.color.grey);
        // setActiveIndicatorColor(R.color.white);

        // Set finish button text
        setFinishButtonTitle("Get Started");

        // Set the finish button style
        // setFinishButtonDrawableStyle(ContextCompat.getDrawable(this, R.drawable.rounded_button));

        setOnboardPages(getOnBoardingCards());
    }

    @Override
    public void onFinishButtonPressed() {
        Intent moveToSignup = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(moveToSignup);
    }

    public List<AhoyOnboarderCard> getOnBoardingCards() {

        List<AhoyOnboarderCard> pages = new ArrayList<>();
        pages.add(new AhoyOnboarderCard("Title",
                "Description",
                R.drawable.ic_action_action_search));
        pages.add(new AhoyOnboarderCard("Title", "Description", R.drawable.ic_action_action_search));
        pages.add(new AhoyOnboarderCard("Title", "Description", R.drawable.ic_action_action_search));

        // TODO: Styling.
        /*
            card1.setBackgroundColor(R.color.black_transparent);
            card1.setTitleColor(R.color.white);
            card1.setDescriptionColor(R.color.grey_200);
            card1.setTitleTextSize(dpToPixels(10, this));
            card1.setDescriptionTextSize(dpToPixels(8, this));
            card1.setIconLayoutParams(iconWidth, iconHeight, marginTop, marginLeft, marginRight, marginBottom);
         */

        return pages;
    }
}
