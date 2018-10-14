package com.hixel.hixel.ui.login;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
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

    private static final String TAG = LoginViewModel.class.getSimpleName();

    private LiveData<User> user;
    private UserRepository repository;

    private boolean isValidUser;
    private MutableLiveData<Boolean> is;
    private LiveData<Boolean> x;

    @Inject
    LoginViewModel(UserRepository repository) {
        this.repository = repository;
    }

    void init() {
        is = new MutableLiveData<>();
        is.setValue(true);
        x = is;
    }

    LiveData<Boolean> getX() {
        return x;
    }


    void verifyUser(String email, String password) {
        Call<Void> call = Client.getClient()
                .create(ServerInterface.class)
                .login(new LoginData(email, password));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,
                    @NonNull Response<Void> response) {
                switch (response.code()) {
                    case 200:
                        is.setValue(true);
                        setIsValidUser(true);
                        break;
                    case 401:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

            }
        });
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }
    }

    public boolean getIsValidUser() {
        return isValidUser;
    }

    public void setIsValidUser(boolean isValid) {
        isValidUser = isValid;
    }

}
