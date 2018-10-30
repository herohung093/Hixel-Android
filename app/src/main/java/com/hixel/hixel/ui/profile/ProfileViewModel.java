package com.hixel.hixel.ui.profile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import com.hixel.hixel.data.UserRepository;
import com.hixel.hixel.data.api.Client;
import com.hixel.hixel.data.api.ServerInterface;
import com.hixel.hixel.data.entities.user.User;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ViewModel for interacting between the UserRepository and ProfileActivity, exposes the User
 * data to the UI and updates any changes made by the user via the UserRepository.
 */
public class ProfileViewModel extends ViewModel {

    private LiveData<User> user;
    private final UserRepository repository;

    @Inject
    ProfileViewModel(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Checks if the user object is null, if it is then it calls the repository to supply
     * the current user.
     */
    void init() {
        if (this.user != null) {
            return;
        }

        user = repository.getUser();
    }

    public LiveData<User> getUser() {
        return user;
    }

    /**
     * Method validates the users password
     * @return The boolean resulting from the validity of the password.
     */
    boolean isValidPassword(String first, String second) {
        return first.equals(second) && first.length() < 4;
    }

    /**
     * Takes the old and new password and updates to the new password.
     *
     * @param oldPassword The users password before the change.
     * @param newPassword The users new password after the change.
     */
    void updateUserPassword(String oldPassword, String newPassword) {
        Call<Void> call = Client
            .getClient().create(ServerInterface.class).changePassword(oldPassword,newPassword);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                // do something to close the dialog
                System.out.println("Response code from change password:"+ response.code());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

            }
        });
    }
   }

