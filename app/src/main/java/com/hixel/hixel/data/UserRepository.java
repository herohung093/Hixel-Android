package com.hixel.hixel.data;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import com.hixel.hixel.data.api.ServerInterface;
import com.hixel.hixel.data.database.UserDao;
import com.hixel.hixel.data.entities.User;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Exposes User data to the ViewModels and saves necessary information in the AppDatabase.
 */
@Singleton
public class UserRepository {

    private ServerInterface serverInterface;
    private final UserDao userDao;
    private final Executor executor;

    @Inject
    public UserRepository(ServerInterface serverInterface, UserDao userDao, Executor executor) {
        this.serverInterface = serverInterface;
        this.userDao = userDao;
        this.executor = executor;
    }

    public LiveData<User> getUser() {
        saveUser();

        return userDao.getUser();
    }

    /**
     * Calls the server for user data information, if successful  returns a boy.
     */
    public void saveUser() {
        serverInterface.userData().enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                executor.execute(() -> {
                    User user = response.body();
                    userDao.saveUser(user);
                });
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {

            }
        });
    }

    /**
     * Updates the user object, we have to pass the entire object to Room as single updates
     * become more expensive.
     *
     * @param user The updates user object.
     */
    public void updateUser(User user) {
        executor.execute(() -> userDao.updateUser(user));
    }

    public void updateUserPassword(String oldPassword, String newPassword) {
        executor.execute(() -> serverInterface.changePassword(oldPassword, newPassword)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call,
                            @NonNull Response<Void> response) { }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) { }
                })
        );
    }
}
