package com.hixel.hixel.dashboard;

import android.support.v7.widget.RecyclerView;

interface RecyclerItemTouchHelperListner {
    void onSwiped(RecyclerView.ViewHolder viewHolder,int direction,int postition);
}
