package com.hixel.hixel.ui.login;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.UserRepository;
import com.hixel.hixel.data.entities.User;
import javax.inject.Inject;

/**
 *  ViewModel for Login related activities, manages saving User to the db and validating
 *  user information.
 */
public class LoginViewModel extends ViewModel {

    private LiveData<User> user;
    private UserRepository repository;

    /**
     * Instantiates the LoginViewModel and injects the UserRepository
     *
     * @param repository The UserRepository
     */
    @Inject
    LoginViewModel(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Method saves the User to the db via the UserRepository
     */
    void saveUser() {
        repository.saveUser();
    }

    boolean isValidEmail(String email) {
        boolean isValid = true;

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = false;
        }

        return isValid;
    }

    boolean isValidPassword(String password) {
        boolean isValid = true;

        if (password.isEmpty() || password.length() < 4) {
            isValid = false;
        }

        return isValid;
    }

    boolean isValidName(String name) {
        return name.isEmpty();
    }
}
