package com.hixel.hixel.data;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import com.hixel.hixel.data.api.ServerInterface;
import com.hixel.hixel.data.database.UserDao;
import com.hixel.hixel.data.entities.user.Portfolio;
import com.hixel.hixel.data.entities.user.User;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Exposes User data to the ViewModels and saves necessary information in the AppDatabase.
 */
@Singleton
public class UserRepository {

    private ServerInterface serverInterface;
    private final UserDao userDao;
    private final Executor executor;

    /**
     * Constructor that gets an instance of the server for api calls, user dao to store user data,
     * and executor to perform operations off the main UI thread.
     *
     * @param serverInterface to make api calls
     * @param userDao to save data to Room
     * @param executor to perform operations off the main UI thread.
     */
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
     * Calls the server for user data information, if successful return a user
     */
    public void saveUser() {
        serverInterface.userData().enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                executor.execute(() -> {
                    User user = response.body();
                    Timber.d("GOT A USER");
                    userDao.saveUser(user);
                });
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) { }
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

    /**
     * Updates the users password, note that this does not involve a database operation as we
     * don't want to store password information.
     *
     * @param oldPassword the users old password
     * @param newPassword the users new password
     */
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

    public void addCompany(List<String> ticker) {
        String tickerString = StringUtils.join(ticker, ",");

        serverInterface.addCompany(tickerString).enqueue(new Callback<Portfolio>() {
            @Override
            public void onResponse(Call<Portfolio> call, Response<Portfolio> response) {
                executor.execute(() -> {
                    User user = userDao.get();
                    user.setPortfolio(response.body());
                    userDao.saveUser(user);
                });
            }

            @Override
            public void onFailure(Call<Portfolio> call, Throwable t) {
                Timber.d("FAILED");
            }
        });
    }
}
