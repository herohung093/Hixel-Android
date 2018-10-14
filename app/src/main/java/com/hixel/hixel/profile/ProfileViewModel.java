package com.hixel.hixel.profile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import com.hixel.hixel.data.UserRepository;
import com.hixel.hixel.data.entities.User;
import javax.inject.Inject;

public class ProfileViewModel extends ViewModel {

    @SuppressWarnings("unused")
    private static final String TAG = ProfileViewModel.class.getSimpleName();

    private LiveData<User> user;
    private UserRepository repository;

    @Inject
    ProfileViewModel(UserRepository repository) {
        this.repository = repository;
    }

    public void init() {
        if (this.user != null) {
            return;
        }

        // user = repository.getUser();
    }

    public LiveData<User> getUser() {
        return user;
    }
    /*
    public void setUserName() {
        repository.setUserFirstName();
    }

    public void setUserEmail() {
        repository.setUserEmail();
    }

    public void setUserPassword() {
        repository.setUserPassword();
    }
    */
}
