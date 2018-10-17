package com.hixel.hixel.data.api;

import static com.hixel.hixel.data.api.Const.BASE_URL;
import static com.hixel.hixel.data.api.Const.REQUEST_TIMEOUT;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hixel.hixel.data.entities.Company;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Provides a Retrofit client to interface with the api, authorisation, deserialization etc. are
 * handled by other helper classes.
 */
public class Client {

    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient;

    /**
     * Initialises a retrofit instance.
     *
     * @return The retrofit instance.
     */
    public static synchronized Retrofit getClient() {

        if (okHttpClient == null) {
            initOkHttp();
        }

        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Company.class, new CompanyDeserializer<Company>())
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

        }

        return retrofit;
    }

    /**
     * Method initialises an OkHttp instance
     */
    private static void initOkHttp() {
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new TokenInterceptor())
                .authenticator(new TokenAuthenticator());

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(Level.BODY);
        httpClient.addInterceptor(loggingInterceptor);

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Request-Type", "Android")
                    .addHeader("Content-Type", "application/json");

            Request request = requestBuilder.build();

            return chain.proceed(request);
        });

        okHttpClient = httpClient.build();
    }
}
