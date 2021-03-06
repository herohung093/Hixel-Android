package com.hixel.hixel.ui.login;

import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.UserRepository;
import com.hixel.hixel.data.api.Client;
import com.hixel.hixel.data.api.ServerInterface;
import javax.inject.Inject;

/**
 *  ViewModel for Login related activities, manages saving User to the db and validating
 *  user information.
 */
public class LoginViewModel extends ViewModel {

    private final UserRepository repository;

    /**
     * Instantiates the LoginViewModel and injects the UserRepository
     *
     * @param repository The UserRepository
     */
    @Inject
    LoginViewModel(UserRepository repository) {
        this.repository = repository;
        this.repository.deleteUser();
    }

    /**
     * Performs logic checks to ensure the user entered email is valid.
     *
     * @param email The email entered in by the user.
     * @return A boolean indicating whether the password is valid.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean isValidEmail(String email) {
        boolean isValid = true;

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = false;
        }

        return isValid;
    }

    /**
     * Performs logic checks to ensure the user entered the email correctly.
     *
     * @param password The user entered password.
     * @return A boolean indicating whether the password is valid.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean isValidPassword(String password) {
        boolean isValid = true;

        if (password.isEmpty() || password.length() < 4) {
            isValid = false;
        }

        return isValid;
    }

    /**
     * Ensures the name meets validation requirements
     *
     * @param name The user entered name.
     * @return A boolean indicating whether the user enter name is valid
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean isValidName(String name) {
        return !name.isEmpty();
    }
}
