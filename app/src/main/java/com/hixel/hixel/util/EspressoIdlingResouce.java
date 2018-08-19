package com.hixel.hixel.util;


import android.support.test.espresso.IdlingResource;
import java.util.concurrent.atomic.AtomicInteger;

// NOTE: THIS CLASS IS UNFINISHED, DO NOT USE!
// Use this class to tell Espresso when to wait for network calls and what not.
public class EspressoIdlingResouce implements IdlingResource {

    private final String mResourceName;

    private final AtomicInteger counter = new AtomicInteger(0);

    private volatile ResourceCallback resourceCallback;

    public EspressoIdlingResouce(String resourceName) {
        mResourceName = resourceName;
    }

    @Override
    public String getName() {
        return mResourceName;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }

    @Override
    public boolean isIdleNow() {
        return counter.get() == 0;
    }

    public void increment() {
        counter.getAndIncrement();
    }


    public void decrement() {
        int counterVal = counter.decrementAndGet();

        if (counterVal == 0) {
            // Going from non-zero to zero indicates idling.
            if (null != resourceCallback) {
                resourceCallback.onTransitionToIdle();
            }
        }

        // If the counter falls below zero, we have a definite problem
        // so throw an exception
        if (counterVal < 0) {
            throw new IllegalArgumentException("Counter has been corrupted");
        }
    }


}
