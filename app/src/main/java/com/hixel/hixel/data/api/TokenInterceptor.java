package com.hixel.hixel.data.api;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.hixel.hixel.App;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.hixel.hixel.data.api.Const.NO_AUTHENTICATION;

/**
 *  Intercepts requests to the server, adds authorisation.
 */
@Singleton
public class TokenInterceptor implements Interceptor {

    @Inject
    public TokenInterceptor() {}

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();

        Request.Builder requestBuilder = request.newBuilder();
        boolean needsCredentials = request.header(NO_AUTHENTICATION) == null;

        SharedPreferences preferences = App.preferences();
        String authToken = preferences.getString("AUTH_TOKEN", null);

        if (needsCredentials) {
            requestBuilder.addHeader("Authorization", "Bearer " + authToken);
        }
        return chain.proceed(requestBuilder.build());
    }
}