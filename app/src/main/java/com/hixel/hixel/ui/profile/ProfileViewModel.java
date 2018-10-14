package com.hixel.hixel.ui.profile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
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

    void init() {
        if (this.user != null) {
            return;
        }

        user = repository.getUser();
    }

    public LiveData<User> getUser() {
        return user;
    }
}
