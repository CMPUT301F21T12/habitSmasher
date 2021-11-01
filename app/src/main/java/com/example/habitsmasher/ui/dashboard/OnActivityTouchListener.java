package com.example.habitsmasher.ui.dashboard;

import android.view.MotionEvent;

/**
 * Interface required to implement the ability for user to swipe and spawn buttons cleanly on a individual view
 * Implementation of this interface came from this source:
 * Name: Velmurugan
 * Date: March 4, 2021
 * URL: https://howtodoandroid.com/android-recyclerview-swipe-menu
 */
public interface OnActivityTouchListener {
    void getTouchCoordinates(MotionEvent ev);
}