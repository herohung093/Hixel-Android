package com.hixel.hixel;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * General utility method for conducting Espresso tests
 */
public class EspressoTestUtil {

       // NOTE: We need a different animations disabler for Fragments.d

    public static void traverseViews(View view) {
        if (view instanceof ViewGroup) {
            traverseViewGroup((ViewGroup) view);
        } else {
            if (view instanceof ProgressBar) {
                disableProgressBarAnimations((ProgressBar) view);
            }
        }
    }

    private static void traverseViewGroup(ViewGroup viewGroup) {
        if (viewGroup instanceof RecyclerView) {
            disableRecyclerViewAnimations((RecyclerView) viewGroup);
        } else {
            final int count = viewGroup.getChildCount();

            for (int i = 0; i < count; i++) {
                traverseViews(viewGroup.getChildAt(i));
            }
        }
    }

    private static void disableRecyclerViewAnimations(RecyclerView recyclerView) {
        recyclerView.setItemAnimator(null);
    }

    private static void disableProgressBarAnimations(ProgressBar progressBar) {
        progressBar.setIndeterminateDrawable(new ColorDrawable(Color.BLUE));
    }

}
