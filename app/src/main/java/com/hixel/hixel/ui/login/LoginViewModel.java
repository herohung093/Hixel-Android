package com.hixel.hixel.ui.login;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.UserRepository;
import com.hixel.hixel.data.entities.User;
import javax.inject.Inject;

public class LoginViewModel extends ViewModel {

    private static final String TAG = LoginViewModel.class.getSimpleName();

    private LiveData<User> user;
    private UserRepository repository;


    @Inject
    LoginViewModel(UserRepository repository) {
        this.repository = repository;
    }

    void init() { }

    void saveUser() {
        repository.saveUser();
    }
}
