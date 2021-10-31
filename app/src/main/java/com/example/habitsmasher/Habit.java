package com.example.habitsmasher;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

/**
 * This is the Habit class
 * Its purpose is to store and retrieve the title, reason, and date of a given habit
 */
public class Habit implements Serializable {
    private String _title;
    private String _reason;
    private Date _date;
    private long _habitId;
    private String _days;

    public Habit () {
        // needed for firestore
    }

    public Habit (String title, String reason, Date date, String days long habitId) {
        _habitId = habitId;
        _title = title;
        _reason = reason;
        _date = date;
        _days = days;
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


    /**
     * Gets the days of the week the habit takes place
     * @return _days : The days the habit takes place
     */
    @PropertyName("days")
    public String getDays() {return _days;}

    /**
     * Sets the days of the week the habit takes place
     * @param days : The new days of the week
     */
    public void setDays(String days){
        _days = days;
    }

    /**
     *
     * @return _habitId: the ID of the habit
     */
    @PropertyName("habitId")
    public long getHabitId() {
        return _habitId;
    }

    // should NEVER be used in practice, adding since it might be needed for _snapshots
    public void setHabitId(long habitId) {
        _habitId = habitId;
    }

}
