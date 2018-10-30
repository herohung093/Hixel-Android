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
    private boolean shouldRefresh = true;
    private final List<Ticker> tickers = new ArrayList<>();

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
        if (shouldRefresh) {
            saveUser();
            shouldRefresh = false;
        }

        return userDao.getUser();
    }


    /**
     * Calls the server for user data information, if successful return a user
     */
    private void saveUser() {
        serverInterface.userData().enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                executor.execute(() -> {
                    User user = response.body();

                    if (user == null)
                        return;

                    Portfolio portfolio = user.getPortfolio();

                    if (portfolio == null)
                        return;

                    portfolio.setCompanies(tickers);

                    Timber.d("INITIAL USER LOAD");
                    Timber.d(user.getEmail());
                    Timber.d("TICKERS:");
                    for (Ticker t : user.getPortfolio().getCompanies()) {
                        Timber.d(t.getTicker());
                    }


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

            Timber.d("ADDING A USER");
            Timber.d(user.getEmail());
            Timber.d("TICKERS:");
            for (Ticker t1 : user.getPortfolio().getCompanies()) {
                Timber.d(t1.getTicker());
            }
            tickers.add(t);

            addToServer(ticker);
        });
    }

    private void addToServer(String ticker) {
        serverInterface.addCompany(ticker).enqueue(new Callback<Portfolio>() {
            @Override
            public void onResponse(@NonNull Call<Portfolio> call, @NonNull Response<Portfolio> response) {
                // TODO: Replace the current portfolio with the returned portfolio
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

            Timber.d("DELETING A USER");
            Timber.d(user.getEmail());
            Timber.d("TICKERS:");
            for (Ticker t1 : user.getPortfolio().getCompanies()) {
                Timber.d(t1.getTicker());
            }

            tickers.removeIf(tick -> t.getTicker().equals(tick.getTicker()));

            removeFromServer(ticker);
        });
    }

    private void removeFromServer(String ticker) {
        serverInterface.removeCompany(ticker).enqueue(new Callback<Portfolio>() {
            @Override
            public void onResponse(@NonNull Call<Portfolio> call, @NonNull Response<Portfolio> response) {
                // TODO: Replace the current portfolio with the returned portfolio
            }

            @Override
            public void onFailure(@NonNull Call<Portfolio> call, @NonNull Throwable t) {
                // TODO: Handle failure
                // The company is already removed on the frontend.
                // If the network call fails, the call can either be queued and retried or the company can just be added back on the frontend.
            }
        });
    }


    @SuppressWarnings("unused")
    public void deleteAllUserTickers() {
        executor.execute(()
                -> serverInterface.removeCompany("TSLA").enqueue(
                new Callback<Portfolio>() {
                    @Override
                    public void onResponse(@NonNull Call<Portfolio> call, @NonNull Response<Portfolio> response) {
                        // DON'T DO ANYTHING YET

                        executor.execute(userDao::deleteAll);

                    }

                    @Override
                    public void onFailure(@NonNull Call<Portfolio> call, @NonNull Throwable t) {
                        Timber.d("API CALL ERROR DELETING TICKERS");
                    }
                }));
    }
}
