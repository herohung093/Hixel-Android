package com.hixel.hixel.ui.profile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import com.hixel.hixel.data.UserRepository;
import com.hixel.hixel.data.api.Client;
import com.hixel.hixel.data.api.ServerInterface;
import com.hixel.hixel.data.entities.user.User;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ViewModel for interacting between the UserRepository and ProfileActivity, exposes the User
 * data to the UI and updates any changes made by the user via the UserRepository.
 */
public class ProfileViewModel extends ViewModel {

    private LiveData<User> user;
    private final UserRepository repository;

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

    public LiveData<User> getUser() {
        return user;
    }

    public void deleteUserData()
    {
        repository.deleteUser();
    }
}

