package com.hixel.hixel.di.modules;

import android.support.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hixel.hixel.data.api.CompanyDeserializer;
import com.hixel.hixel.data.api.RatiosDeserializer;
import com.hixel.hixel.data.api.ServerInterface;
import com.hixel.hixel.data.api.TokenAuthenticator;
import com.hixel.hixel.data.api.TokenInterceptor;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.entities.CompanyData;
import dagger.Module;
import dagger.Provides;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * TODO: Explanation
 */

@Module
public class RepositoryModule {

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

        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[0];
            }
        } };

        // Install the all-trusting trust manager
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
        } catch (Exception ignored) { }

        // Create an ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        // Create a logging interceptor
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(Level.BODY);

        client.connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(tokenInterceptor)
                .authenticator(tokenAuthenticator)
                .sslSocketFactory(sslSocketFactory)
                .addInterceptor(loggingInterceptor)
                .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        client.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Request-Type", "Android")
                    .addHeader("Content-Type", "application/json");

            Request request = requestBuilder.build();

            return chain.proceed(request);
        });

        // Create Gson for custom deserialization
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Company.class, new CompanyDeserializer<Company>())
                .create();

        //add retro builder
        Retrofit.Builder retroBuilder= new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));

        retroBuilder.client(client.build());

        //create retrofit - only this instance would be used in the entire application
        Retrofit retrofit = retroBuilder.build();
        return retrofit;

    }

    /**
     * TODO: explanation
     */

    @Provides
    @Singleton
    @NonNull
    ServerInterface provideServerInterface(Retrofit retrofit) {
        return retrofit.create(ServerInterface.class);
    }

}
