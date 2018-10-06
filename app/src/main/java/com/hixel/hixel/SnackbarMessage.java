package com.hixel.hixel;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.StringRes;

public class SnackbarMessage extends SingleLiveEvent<Integer> {

    public void observe(LifecycleOwner owner, final SnackbarObserver observer) {
        super.observe(owner, i -> {

            if (i == null) {
                return;
            }

            observer.onNewMessage(i);

        });
    }

    public interface SnackbarObserver {
        // Call this when a new message needs to be shown.
        void onNewMessage(@StringRes int snackbarMessageResourceId);
    }
}
