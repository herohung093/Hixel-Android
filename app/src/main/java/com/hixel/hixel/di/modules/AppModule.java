package com.hixel.hixel.di.modules;

import android.app.Application;
import android.arch.persistence.room.Room;
import com.hixel.hixel.data.CompanyRepository;
import com.hixel.hixel.data.UserRepository;
import com.hixel.hixel.data.database.AppDatabase;
import com.hixel.hixel.data.database.CompanyDao;
import com.hixel.hixel.data.api.ServerInterface;
import com.hixel.hixel.data.database.UserDao;
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

    // ***************************************
    // Database Injections
    // **************************************
    @Provides
    @Singleton
    AppDatabase provideDatabase(Application context) {
        return Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "appDB")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    CompanyDao provideCompanyDao(AppDatabase database) {
        return database.companyDao();
    }

    @Provides
    @Singleton
    UserDao provideUserDao(AppDatabase database) {
        return database.userDao();
    }

    // ***************************************
    // Repository Injections
    // **************************************
    @Provides
    Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    @Singleton
    CompanyRepository provideCompanyRepository(ServerInterface serverInterface,
            CompanyDao companyDao, Executor executor) {
        return new CompanyRepository(serverInterface, companyDao, executor);
    }

    @Provides
    @Singleton
    UserRepository provideUserRepository(ServerInterface serverInterface,
            UserDao userDao, Executor executor) {
        return new UserRepository(serverInterface, userDao, executor);
    }
}
