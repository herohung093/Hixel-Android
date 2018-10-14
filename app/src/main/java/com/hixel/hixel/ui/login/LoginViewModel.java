package com.hixel.hixel.ui.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.UserRepository;
import com.hixel.hixel.data.entities.User;
import javax.inject.Inject;

public class LoginViewModel extends ViewModel {

    private LiveData<User> user;
    private UserRepository repository;
    private boolean isUserStale;

    @Inject
    LoginViewModel(UserRepository repository) {
        this.repository = repository;
    }

    public void init() {
        isUserStale = repository.isUserStale();
    }

    public boolean getIsUserStale() {
        return isUserStale;
    }

    public boolean login(String email, String password) {
        return repository.verifyUser(email, password);
    }
}
