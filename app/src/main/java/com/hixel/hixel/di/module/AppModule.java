package com.hixel.hixel.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hixel.hixel.db.CompanyRepository;
import com.hixel.hixel.db.local.AppDatabase;
import com.hixel.hixel.db.local.CompanyDao;
import com.hixel.hixel.db.remote.ServerInterface;
import dagger.Module;
import dagger.Provides;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.inject.Singleton;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

// TODO: Seperate the modules out
// This Module provides context to our other modules
@Module(includes = ViewModelModule.class)
public class AppModule {

    // DEPENDENCY INJECTION

    @Provides
    @Singleton
    AppDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application,
                AppDatabase.class, "MyDatabase.db")
                .build();
    }

    @Provides
    @Singleton
    CompanyDao provideUserDao(AppDatabase database) { return database.companyDao(); }

    // REPOSITORY INJECTION

    @Provides
    Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    @Singleton
    CompanyRepository provideUserRepository(ServerInterface webservice, CompanyDao userDao, Executor executor) {
        return new CompanyRepository(webservice, userDao, executor);
    }

    // NETWORK INJECTION

    private static final String BASE_URL = "https://game.bones-underground.org:8443";

    @Provides
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    @Provides
    @Singleton
    ServerInterface provideRetrofitService(Retrofit retrofit) {
        return retrofit.create(ServerInterface.class);
    }
}
