package com.example.habitsmasher.listeners;

import android.util.Log;
import android.view.View;

import com.example.habitsmasher.DaysTracker;

public class ClickListenerForDaysOfTheWeek implements View.OnClickListener{
    private DaysTracker _tracker;

    public ClickListenerForDaysOfTheWeek(DaysTracker tracker, String day) {
        _tracker = tracker;
        _day = day;
    }

    private String _day;

    @Override
    public void onClick(View view) {
        switch (_day) {
            case "SU":
                if (_tracker.getSunday()) {
                    _tracker.setSunday(false);
                } else {
                    _tracker.setSunday(true);
                }
                break;
            case "MO":
                if (_tracker.getMonday()) {
                    _tracker.setMonday(false);
                } else {
                    _tracker.setMonday(true);
                }
                break;
            case "TU":
                if (_tracker.getTuesday()) {
                    _tracker.setTuesday(false);
                } else {
                    _tracker.setTuesday(true);
                }
                break;
            case "WE":
                if (_tracker.getWednesday()) {
                    _tracker.setWednesday(false);
                } else {
                    _tracker.setWednesday(true);
                }
                break;
            case "TH":
                if (_tracker.getThursday()) {
                    _tracker.setThursday(false);
                } else {
                    _tracker.setThursday(true);
                }
                break;
            case "FR":
                if (_tracker.getFriday()) {
                    _tracker.setFriday(false);
                } else {
                    _tracker.setFriday(true);
                }
                break;
            case "SA":
                if (_tracker.getSaturday()) {
                    _tracker.setSaturday(false);
                } else {
                    _tracker.setSaturday(true);
                }
                break;
        }
        Log.d("Tracker Status", _tracker.getDays());
    }
}
