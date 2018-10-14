package com.hixel.hixel.ui.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.util.Log;
import com.hixel.hixel.data.UserRepository;
import com.hixel.hixel.data.api.Client;
import com.hixel.hixel.data.api.ServerInterface;
import com.hixel.hixel.data.entities.User;
import com.hixel.hixel.data.models.LoginData;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private LiveData<User> user;
    private UserRepository repository;

    private boolean isValidUser = false;

    @Inject
    LoginViewModel(UserRepository repository) {
        this.repository = repository;
    }

    public void init() { }

    public void verifyUser(String email, String password) {
        Call<Void> call = Client.getClient()
                .create(ServerInterface.class)
                .login(new LoginData(email, password));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,
                    @NonNull Response<Void> response) {
                switch (response.code()) {
                    case 200:
                        loginSuccess();
                        break;
                    case 401:
                        loginFailed();
                        break;
                    default:
                        loginFailed();
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
            }
        });
    }

    public boolean getIsValidUser() {
        return isValidUser;
    }

    public void loginSuccess() {
        isValidUser = true;
    }

    public void loginFailed() {
        isValidUser = false;
    }
}
