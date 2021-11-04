package com.example.habitsmasher.ui.dashboard;

import android.view.MotionEvent;

/**
 * Listener interface required for RecyclerTouchListener class
 * Implementation of interface came from this source:
 * Name: Velmurugan
 * Date: March 4, 2021
 * URL: https://howtodoandroid.com/android-recyclerview-swipe-menu
 */
public interface OnActivityTouchListener {
    void getTouchCoordinates(MotionEvent ev);
}