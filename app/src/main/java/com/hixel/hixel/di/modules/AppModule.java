package com.hixel.hixel.di.modules;

import android.app.Application;
import android.arch.persistence.room.Room;
import com.hixel.hixel.data.CompanyRepository;
import com.hixel.hixel.data.database.AppDatabase;
import com.hixel.hixel.data.database.CompanyDao;
import com.hixel.hixel.data.api.ServerInterface;
import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjectionModule;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.inject.Singleton;

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
                AppDatabase.class, "companies.db")
                // TODO: Better migration
                .fallbackToDestructiveMigration()
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
}
