package com.example.habitsmasher;

import java.util.Date;

/**
 * This is the Habit class
 * Its purpose is to store and retrieve the title, reason, and date of a given habit
 */
public class Habit {
    private String _title;
    private String _reason;
    private Date _date;

    /**
     *
     * @return _title : The title of the habit
     */
    public String get_title() {
        return _title;
    }

    /**
     *
     * @param title : The new title to be set
     */
    public void set_title(String title) {
        this._title = title;
    }

    /**
     *
     * @return _reason: The reason of the habit
     */
    public String get_reason() {
        return _reason;
    }

    /**
     *
     * @param reason : The new reason to be set
     */
    public void set_reason(String reason) {
        this._reason = reason;
    }

    /**
     *
     * @return _date : The date of the habit
     */
    public Date get_date() {
        return _date;
    }

    /**
     *
     * @param date : The new date to be set
     */
    public void set_date(Date date) {
        this._date = date;
    }

    public Habit (String title, String reason, Date date) {
        _title = title;
        _reason = reason;
        _date = date;
    }
}
