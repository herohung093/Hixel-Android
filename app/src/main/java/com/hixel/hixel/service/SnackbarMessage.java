package com.hixel.hixel.service;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import com.hixel.hixel.SingleLiveEvent;

public class SnackbarMessage extends SingleLiveEvent<Integer> {

    public void observe(LifecycleOwner owner, final SnackbarObserver observer) {
        super.observe(owner, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer t) {
                if (t == null) {
                    return;
                }

                observer.onNewMessage(t);
            }
        });
    }

    public interface SnackbarObserver {

        // Call this when a new message needs to be shown.
        void onNewMessage(@StringRes int snackbarMessageResourceId);
    }

}
