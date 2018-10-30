package com.hixel.hixel.di.modules;

import android.app.Application;
import android.arch.persistence.room.Room;
import com.hixel.hixel.AppExecutors;
import com.hixel.hixel.data.CompanyRepository;
import com.hixel.hixel.data.UserRepository;
import com.hixel.hixel.data.database.AppDatabase;
import com.hixel.hixel.data.api.ServerInterface;
import com.hixel.hixel.data.database.FinancialDataEntryDao;
import com.hixel.hixel.data.database.IdentifiersDao;
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
 *
 * The class also provides the RoomDB and Repositories to the application.
 */
@Module(includes = {AndroidInjectionModule.class, ViewModelModule.class})
public class AppModule {

    // ***************************************
    // Database Injections
    // **************************************

    /**
     * Provides an instance of the application database.
     *
     * @param context the Application context
     * @return the application database
     */
    @Provides
    @Singleton
    AppDatabase provideDatabase(Application context) {
        return Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "appDB")
                .fallbackToDestructiveMigration()
                .build();
    }


    /**
     * Provides and instance of the user dao
     *
     * @param database the application database
     * @return the user dao
     */
    @Provides
    @Singleton
    UserDao provideUserDao(AppDatabase database) {
        return database.userDao();
    }

    @Provides
    @Singleton
    IdentifiersDao provideIdentifiersDao(AppDatabase database) { return database.identifiersDao(); }


    @Provides
    @Singleton
    FinancialDataEntryDao provideFinancialDataEntryDao(AppDatabase database) { return database.financialDataEntryDao(); }


    // ***************************************
    // Repository Injections
    // **************************************

    /**
     * Provides an instance of an executor to perform operations off the main UI thread.
     *
     * @return an executor
     */
    @Provides
    Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    /**
     * Returns an instance of the company repository which acts as a single source for interacting
     * with Company data.
     *
     * @param serverInterface for api calls
     * @param appExecutors executor for off UI thread operations
     * @return the company repository
     */
    @Provides
    @Singleton
    CompanyRepository provideCompanyRepository(ServerInterface serverInterface, IdentifiersDao identifiersDao,
            FinancialDataEntryDao financialDataEntryDao, AppExecutors appExecutors) {
        return new CompanyRepository(serverInterface, identifiersDao, financialDataEntryDao, appExecutors);
    }

    /**
     * Returns an instance of the user repository which acts as a single source for interacting
     * with the User data.
     *
     * @param serverInterface for api calls
     * @param userDao for database operations
     * @param executor for executing off the main UI thread
     * @return the user repository.
     */
    @Provides
    @Singleton
    UserRepository provideUserRepository(ServerInterface serverInterface,
            UserDao userDao, Executor executor) {
        return new UserRepository(serverInterface, userDao, executor);
    }
}
