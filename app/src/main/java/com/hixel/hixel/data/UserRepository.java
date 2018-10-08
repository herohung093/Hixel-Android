package com.hixel.hixel.data;


import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import com.hixel.hixel.data.api.ServerInterface;
import com.hixel.hixel.data.database.UserDao;
import com.hixel.hixel.data.entities.User;
import com.hixel.hixel.data.models.LoginData;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class UserRepository {

    private static final int FRESH_TIMEOUT_IN_MINUTES = 10;

    @SuppressWarnings("unused")
    private static final String TAG = UserRepository.class.getSimpleName();

    private ServerInterface serverInterface;
    private final UserDao userDao;
    private final Executor executor;

    // TEMPORARY WORKAROUND
    private boolean isVerified;
    private boolean isStale;

    @Inject
    public UserRepository(ServerInterface serverInterface, UserDao userDao, Executor executor) {
        this.serverInterface = serverInterface;
        this.userDao = userDao;
        this.executor = executor;
    }

    public LiveData<User> getUser() {
        // TODO: Think of a way to do refreshes for a user.
        // refreshUser();

        executor.execute(() -> {
            User user = userDao.get();
            Log.d(TAG, "getUser: " + user.getEmail());
        });

        return userDao.getUser();
    }

    public boolean verifyUser(String userEmail, String userPassword) {
        serverInterface.login(new LoginData(userEmail, userPassword))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.code() == 200) {
                            isVerified = true;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

                    }
                });

        Log.d(TAG, "verifyUser: " + isVerified);

        return isVerified;
    }

    public boolean isUserStale() {
        executor.execute(() -> isStale = (userDao.isStaleUser("test@gmail.com", getMaxRefreshTime(new Date())) != 1));

        Log.d(TAG, "isUserStale: " + isStale);

        return isStale;
    }

    private void refreshUser(String userEmail, String userPassword, String firstName, String lastName) {
        executor.execute(() -> {
            boolean userExists = (userDao.hasUser(userEmail, getMaxRefreshTime(new Date())) == 1);

            if (!userExists) {
                // TODO: Remove login data model and just send two strings.
                serverInterface.login(new LoginData(userEmail, userPassword))
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                                switch (response.code()) {
                                    case 200:
                                        User user = new User(userEmail, userPassword, firstName, lastName, new Date());
                                        userDao.saveUser(user);
                                        break;
                                    case 401:
                                        Log.d(TAG, "onResponse: Incorrect username or password");
                                        break;
                                    default:
                                        Log.d(TAG, "onResponse: An unknown error occurred trying to access the server.");
                                        break;
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                // TODO: Error handling.
                            }
                        });
            }
        });
    }

    private Date getMaxRefreshTime(Date currentDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.MINUTE, -FRESH_TIMEOUT_IN_MINUTES);

        return cal.getTime();
    }
}
