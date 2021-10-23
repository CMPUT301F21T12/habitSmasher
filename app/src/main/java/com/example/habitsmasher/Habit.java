package com.example.habitsmasher;

import com.google.firebase.firestore.PropertyName;

import java.util.Date;

/**
 * This is the Habit class
 * Its purpose is to store and retrieve the title, reason, and date of a given habit
 */
public class Habit {
    // very rough implementation of a unique ID used to identify habits in the database
    private static int _habitIdCounter = 0;
    private int _habitId;
    private String _title;
    private String _reason;
    private Date _date;

    public Habit () {
        // needed for firestore
    }

    public Habit (String title, String reason, Date date) {
        _habitId = _habitIdCounter;
        _title = title;
        _reason = reason;
        _date = date;
        _habitIdCounter++;
    }

    /**
     *
     * @return _title : The title of the habit
     */
    @PropertyName("title")
    public String getTitle() {
        return _title;
    }

    /**
     *
     * @param title : The new title to be set
     */
    public void setTitle(String title) {
        _title = title;
    }

    /**
     *
     * @return _reason: The reason of the habit
     */
    @PropertyName("reason")
    public String getReason() {
        return _reason;
    }

    /**
     *
     * @param reason : The new reason to be set
     */
    public void setReason(String reason) {
        _reason = reason;
    }

    /**
     *
     * @return _date : The date of the habit
     */
    @PropertyName("date")
    public Date getDate() {
        return _date;
    }

    /**
     *
     * @param date : The new date to be set
     */
    public void setDate(Date date) {
        _date = date;
    }

    public int getHabitId() {
        return _habitId;
    }
}
