package com.hixel.hixel.di.modules;

import android.app.Application;
import android.arch.persistence.room.Room;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hixel.hixel.data.source.CompanyRepository;
import com.hixel.hixel.data.source.local.AppDatabase;
import com.hixel.hixel.data.source.local.CompanyDao;
import com.hixel.hixel.service.network.ServerInterface;
import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjectionModule;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.inject.Singleton;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Use this to bind our Application class as a Context in the AppComponent.
 * This puts the Application & Activities into the graph. No need to pass the
 * Application instance to modules.
 */

@Module(includes = {AndroidInjectionModule.class, ViewModelModule.class})
public class AppModule {

    // --- DATABASE INJECTION ---

    @Provides
    @Singleton
    AppDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application,
                AppDatabase .class, "companies.db")
                .build();
    }

    @Provides
    @Singleton
    CompanyDao provideUserDao(AppDatabase database) { return database.companyDao(); }

    // --- REPOSITORY INJECTION ---

    @Provides
    Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    @Singleton
    CompanyRepository provideUserRepository(ServerInterface serverInterface, CompanyDao companyDao, Executor executor) {
        return new CompanyRepository(serverInterface, companyDao, executor);
    }

    // --- NETWORK INJECTION ---
    private static final String BASE_URL = "https://game.bones-underground.org:8443";

    @Provides
    @Singleton
    Cache provideHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient(Cache cache) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.cache(cache);
        return client.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    ServerInterface provideServerInterface(Retrofit retrofit) {
        return retrofit.create(ServerInterface.class);
    }
}
