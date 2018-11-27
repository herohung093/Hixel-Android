package com.hixel.hixel.data;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.hixel.hixel.data.api.ServerInterface;
import com.hixel.hixel.data.database.UserDao;
import com.hixel.hixel.data.entities.user.Portfolio;
import com.hixel.hixel.data.entities.user.Ticker;
import com.hixel.hixel.data.entities.user.User;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Exposes User data to the ViewModels and saves necessary information in the AppDatabase.
 */
@Singleton
public class UserRepository {

    private final ServerInterface serverInterface;
    private final UserDao userDao;
    private final Executor executor;
    private boolean init = true;

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
        if (init) {
            loadUserFromServer();
            init = false;
        }

        return userDao.getUser();
    }


    /**
     * Calls the server for user data information, if successful return a user
     */
    private void loadUserFromServer() {
        serverInterface.userData().enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                executor.execute(() -> {
                    User user = response.body();

                    if (user == null) {
                        Timber.d("User was null.");
                        return;
                    }
                    Portfolio portfolio = user.getPortfolio();

                    if (portfolio == null) {
                        Timber.d("Portfolio was null.");
                        return;
                    }

                    Timber.d("User loaded from server: %s", user.getEmail());
                    Timber.d("Portfolio: %s", user.getPortfolio().toString());

                    userDao.saveUser(user);
                });
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) { }
        });
    }


    public void addCompany(String ticker) {
        executor.execute(() -> {
            // Get our current user from the db.
            User user = userDao.get();
            // Get the ticker we want to add.
            Ticker t = new Ticker();
            t.setTicker(ticker);

            // Add the ticker to the user.
            user.getPortfolio().getCompanies().add(t);

            // Save the user back into the db.
            userDao.saveUser(user);

            Timber.d("Adding new company to portfolio");
            Timber.d("Portfolio: %s", user.getPortfolio().toString());

            addToServer(ticker);
        });
    }

    private void addToServer(String ticker) {
        serverInterface.addCompany(ticker).enqueue(new Callback<Portfolio>() {
            @Override
            public void onResponse(@NonNull Call<Portfolio> call, @NonNull Response<Portfolio> response) {
                executor.execute(() -> portfolioUpdateCallback(response.body()));
            }

            @Override
            public void onFailure(@NonNull Call<Portfolio> call, @NonNull Throwable t) {
                // TODO: Handle failure
                // The company is already added on the frontend.
                // If the network call fails, the call can either be queued and retried or the company can just be removed on the frontend.
            }
        });
    }

    public void deleteCompany(String ticker) {
        executor.execute(() -> {
            // Get our current user from the db.
            User user = userDao.get();
            // Get the ticker we want to add.
            Ticker t = new Ticker();
            t.setTicker(ticker);

            // Remove the ticker.
            user.getPortfolio().getCompanies().removeIf(tick -> t.getTicker().equals(tick.getTicker()));

            // Save the user back into the db.
            userDao.saveUser(user);

            Timber.d("Deleting a company from the portfolio");
            Timber.d("Portfolio: %s", user.getPortfolio().toString());

            removeFromServer(ticker);
        });
    }

    private void removeFromServer(String ticker) {
        serverInterface.removeCompany(ticker).enqueue(new Callback<Portfolio>() {
            @Override
            public void onResponse(@NonNull Call<Portfolio> call, @NonNull Response<Portfolio> response) {
                executor.execute(() -> portfolioUpdateCallback(response.body()));
            }

            @Override
            public void onFailure(@NonNull Call<Portfolio> call, @NonNull Throwable t) {
                // TODO: Handle failure
                // The company is already removed on the frontend.
                // If the network call fails, the call can either be queued and retried or the company can just be added back on the frontend.
            }
        });
    }

    private void portfolioUpdateCallback(Portfolio portfolioUpdate)
    {
        // Get our current user from the db.
        User user = userDao.get();

        if (portfolioUpdate == null) {
            Timber.w("Received null portfolio update from the server");
            return;
        }

        user.setPortfolio(portfolioUpdate);

        Timber.d("Received portfolio update from the server");
        Timber.d("New Portfolio: %s", portfolioUpdate.toString());

        userDao.saveUser(user);
        Timber.d("Saved");
    }

    public void deleteUser() {
        executor.execute(userDao::deleteAll);
        init = true;
    }
}
