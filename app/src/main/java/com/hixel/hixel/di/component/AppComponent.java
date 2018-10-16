package com.hixel.hixel.di.component;

import android.app.Application;
import com.hixel.hixel.App;
import com.hixel.hixel.di.modules.ActivityBuilderModule;
import com.hixel.hixel.di.modules.AppModule;
import com.hixel.hixel.di.modules.NetModule;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import javax.inject.Singleton;

/**
 * Provides a graph to all of the modules we are using
 */

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        ActivityBuilderModule.class,
        NetModule.class
})
public interface AppComponent extends AndroidInjector<App> {

    /**
     * Application will be providing the dependency graph. To use this:
     * <code>DaggerApplicationComponent.builder().application(this).build().inject(this)</code>
     * Remember this, because it may differ when looking at examples/tutorials.
     */
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

    void inject(App app);
}
