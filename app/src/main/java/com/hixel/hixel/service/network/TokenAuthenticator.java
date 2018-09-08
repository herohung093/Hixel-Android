package com.hixel.hixel.service.network;

import com.hixel.hixel.App;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Route;
import okhttp3.Response;
import retrofit2.Call;

import static com.hixel.hixel.service.network.Const.NO_AUTHENTICATION;

public class TokenAuthenticator implements Authenticator {
    @SuppressLint("ApplySharedPref")
    @Override
    public Request authenticate(@NonNull Route route, @NonNull Response response) throws IOException {
        boolean needsCredentials = response.request().header(NO_AUTHENTICATION) == null;

        if (response.code() == 401 && needsCredentials) {
            SharedPreferences preferences = App.preferences();
            String refreshToken = preferences.getString("REFRESH_TOKEN", null);

            Call<Void> refreshCall = Client.getClient()
                    .create(ServerInterface.class)
                    .refreshAccessToken(refreshToken);

            //NOTE: This is purposely a synchronous call.
            retrofit2.Response<Void> refreshResponse = refreshCall.execute();

            if (refreshResponse != null && refreshResponse.code() == 200) {
                String newAuthToken = refreshResponse.headers().get("Authorization");

                if (newAuthToken != null) {
                    newAuthToken = newAuthToken.replace("Bearer ", "");

                    preferences.edit()
                            .putString("AUTH_TOKEN", newAuthToken)
                            .commit();

                    return response.request().newBuilder()
                            .header("Authorization", "Bearer " + newAuthToken)
                            .build();
                }
            }
        }

        return null;
    }
}