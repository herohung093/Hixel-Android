package com.hixel.hixel;

import static org.junit.Assert.assertEquals;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.hixel.hixel.view.ui.LoginActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(AndroidJUnit4.class)
public class LoginUnitTest {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule =
        new ActivityTestRule(LoginActivity.class);
    @Test
    public void testCheckEmailPattern(){
        String expectedEmail= "name@gmail.com";
        String expectedPass= "2315";
        boolean result=validate(expectedEmail,expectedPass);
        assertEquals("validate failed",true,result);

    }
    
    private boolean validate(String email, String password) {
        boolean valid = true;

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            valid = false;
        }


        if (password.isEmpty() || password.length() < 4) {

            valid = false;
        }

        return valid;
    }
}
