package com.hixel.hixel.di.component;

import android.app.Application;
import com.hixel.hixel.App;
import com.hixel.hixel.di.module.ActivityModule;
import com.hixel.hixel.di.module.AppModule;
import dagger.BindsInstance;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules={ ActivityModule.class, AppModule.class })
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

    void inject(App app);
}
