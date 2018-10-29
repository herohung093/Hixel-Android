package com.hixel.hixel.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;
import com.hixel.hixel.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Onboarding Activity extends AhoyOnboarderActivity
 * Displays UI for the Onboarding.
 */
public class Onboarding extends AhoyOnboarderActivity {

    static String COMPLETED_ONBOARDING_PREF_NAME = "onboarding_initiated";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Integer> colorList = new ArrayList<>();
        /*colorList.add(R.color.onboarding_green);
        colorList.add(R.color.onboarding_blue);
        colorList.add(R.color.onboarding_alt_orange);*/

        colorList.add(R.color.good);
        colorList.add(R.color.average);
        colorList.add(R.color.bad);

        setColorBackground(colorList);

        // Available properties
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        setFont(face);

        // Hide navigation controls
        showNavigationControls(false);

        // Set finish button text
        setFinishButtonTitle(R.string.get_started);

        // Set the finish button style
        setFinishButtonDrawableStyle(
                ContextCompat.getDrawable(this, R.drawable.onboarding_btn_alt));

        setOnboardPages(getOnBoardingCards());
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences.Editor sharedPreferencesEditor =
                PreferenceManager.getDefaultSharedPreferences(this).edit();
        sharedPreferencesEditor.putBoolean(COMPLETED_ONBOARDING_PREF_NAME, true);
        sharedPreferencesEditor.apply();
    }

    @Override
    public void onFinishButtonPressed() {
        Intent moveToSignup = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(moveToSignup);
        finish();
    }

    /**
     * Method constructs a list of onboarding cards.
     *
     * @return A list of Onboarding cards
     */
    public List<AhoyOnboarderCard> getOnBoardingCards() {

        List<AhoyOnboarderCard> cards = new ArrayList<>();
        cards.add(new AhoyOnboarderCard(
                "No crazy analysis!",
                "We measure a company by 5 factors, on a 1-5 scale.",
                R.drawable.onboarding_alternate_barchart)
        );

        cards.add(new AhoyOnboarderCard(
                "Details when it matters",
                "Powerful charts and a comparison tool to go deep when you need it.",
                R.drawable.onboarding_compare)
        );

        cards.add(new AhoyOnboarderCard(
                "Let's Go!",
                "Sign up now and make your own portfolio in minutes!",
                R.drawable.onboarding_launch)
        );


        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setDescriptionColor(R.color.text_main_dark);
            cards.get(i).setTitleColor(R.color.text_main_dark);
            cards.get(i).setBackgroundColor(R.color.white);
        }

        return cards;
    }
}
