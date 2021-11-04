package com.example.habitsmasher;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.Date;

/**
 * This is the Habit class
 * Its purpose is to store and retrieve the title, reason, and date of a given habit
 */
public class Habit extends DatabaseEntity implements Serializable {
    private String _title;
    private String _reason;
    private Date _date;
    private HabitEventList _habitEvents;

    /**
     * Empty constructor needed for FireStore storage
     */
    public Habit () {
        // needed for firestore
    }

    /**
     * Constructs a habit
     * @param title title of habit
     * @param reason reason for habit
     * @param date starting date of habit
     * @param habitId id of habit
     * @param habitEvents list holding habit events
     */
    public Habit (String title, String reason, Date date, long habitId, HabitEventList habitEvents) {
        super(habitId);
        _title = title;
        _reason = reason;
        _date = date;
        _habitEvents = habitEvents;
    }

    /**
     * Gets the title of the habit
     * @return _title : The title of the habit
     */
    @PropertyName("title")
    public String getTitle() {
        return _title;
    }

    /**
     * Sets the title of the habit
     * @param title : The new title to be set
     */
    public void setTitle(String title) {
        _title = title;
    }

    /**
     * Gets the reason of the habit
     * @return _reason: The reason of the habit
     */
    @PropertyName("reason")
    public String getReason() {
        return _reason;
    }

    /**
     * Sets the reason of the habit
     * @param reason : The new reason to be set
     */
    public void setReason(String reason) {
        _reason = reason;
    }

    /**
     * Gets the starting date of the habit
     * @return _date : The starting date of the habit
     */
    @PropertyName("date")
    public Date getDate() {
        return _date;
    }

    /**
     * Sets the starting date of the habit
     * @param date : The new starting date to be set
     */
    public void setDate(Date date) {
        _date = date;
    }

    /**
     * Gets habit event list of a habit
     * @return habitEvents (HabitEventList): The habit events associated with a habit
     */
    @PropertyName("Events")
    public HabitEventList getHabitEvents()  { return _habitEvents; }

    /**
     * Sets the habit events of a habit
     * @param habitEvents (HabitEventList): The list of habit events to set
     */
    public void setHabitEvents(HabitEventList habitEvents) { _habitEvents = habitEvents; }
}
