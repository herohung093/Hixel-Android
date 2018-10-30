package com.hixel.hixel.di.modules;

import android.support.annotation.NonNull;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.hixel.hixel.data.api.ServerInterface;
import com.hixel.hixel.data.api.TokenAuthenticator;
import com.hixel.hixel.data.api.TokenInterceptor;
import com.hixel.hixel.util.LiveDataCallAdapterFactory;
import dagger.Module;
import dagger.Provides;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Provides networking components required for the application, as well as handles authentication.
 */
@Module
public class NetModule {

    private static final String BASE_URL = "https://game.bones-underground.org:8443";
    private static final int REQUEST_TIMEOUT = 60;

    @Provides
    @Singleton
    @NonNull
    OkHttpClient.Builder provideOkHttp() {
        return new Builder();
    }

    @Provides
    @Singleton
    @NonNull
    TokenAuthenticator provideTokenAuthenticator() {
        return new TokenAuthenticator();
    }

    @Provides
    @Singleton
    @NonNull
    TokenInterceptor providesRequestInterceptor() {
        return new TokenInterceptor();
    }

    @Provides
    @Singleton
    @NonNull
    Retrofit provideRetrofit(OkHttpClient.Builder client, TokenInterceptor tokenInterceptor,
            TokenAuthenticator tokenAuthenticator) {
        client.addNetworkInterceptor(new StethoInterceptor())
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(tokenInterceptor)
                .authenticator(tokenAuthenticator);

        client.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Request-Type", "Android")
                    .addHeader("Content-Type", "application/json");

            Request request = requestBuilder.build();

            return chain.proceed(request);
        });

        //add retro builder
        Retrofit.Builder retroBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create());

        retroBuilder.client(client.build());

        //create retrofit - only this instance would be used in the entire application
        return retroBuilder.build();
    }

    /**
     * Provides an instance of the ServerInterface
     */
    @Provides
    @Singleton
    @NonNull
    ServerInterface provideServerInterface(Retrofit retrofit) {
        return retrofit.create(ServerInterface.class);
    }

}
