package com.hixel.hixel.di.component;

import android.app.Application;
import com.hixel.hixel.App;
import com.hixel.hixel.di.modules.ActivityBuilderModule;
import com.hixel.hixel.di.modules.AppModule;
import com.hixel.hixel.di.modules.RepositoryModule;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import javax.inject.Singleton;

/**
 * A Dagger component, it's a singleton so there is only ever one instance.
 * We indicate to the component which modules we are going to use.
 */

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        ActivityBuilderModule.class,
        RepositoryModule.class
})
public interface AppComponent extends AndroidInjector<App> {

    // Application wil be provided the dependency graph. To use this:
    // DaggerApplicationComponent.builder().application(this).build().inject(this)
    // Remember this, because it may differ when looking at examples/tutorials.
    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(App app);
}
