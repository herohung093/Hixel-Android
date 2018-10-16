package com.hixel.hixel.di;

import android.arch.lifecycle.ViewModel;

import dagger.MapKey;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Creates a map of ViewModel class types and ViewModel instances to be passed to the
 * ViewModelFactory. This is used so we only have to have one ViewModelFactory, checkout Dagger2
 * multibindings.
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@MapKey
public @interface ViewModelKey {
    Class<? extends ViewModel> value();
}
