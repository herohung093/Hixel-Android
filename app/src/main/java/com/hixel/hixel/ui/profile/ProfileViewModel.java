package com.hixel.hixel.ui.profile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.UserRepository;
import com.hixel.hixel.data.entities.User;
import javax.inject.Inject;

/**
 * ViewModel for interacting between the UserRepository and ProfileActivity
 */
public class ProfileViewModel extends ViewModel {

    @SuppressWarnings("unused")
    private static final String TAG = ProfileViewModel.class.getSimpleName();

    private LiveData<User> user;
    private UserRepository repository;

    @Inject
    ProfileViewModel(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Checks if the user object is null, if it is then it calls the repository to supply
     * the current user.
     */
    void init() {
        if (this.user != null) {
            return;
        }

        user = repository.getUser();
    }

    /**
     * Method returns a LiveData User.
     * @return A LiveData User
     */
    public LiveData<User> getUser() {
        return user;
    }
}
