package com.example.habitsmasher;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;

public class HabitEventList extends ArrayList{
    private ArrayList<HabitEvent> _habitEvents = new ArrayList<>();

    /**
     * Gets the list of habit events
     * @return habitEvents: The list of habit events
     */
    public ArrayList<HabitEvent> getHabitEvents() {
        return this._habitEvents;
    }

    /**
     * Creates a new habit event and add its to the habit event list
     * @param startDate (Date): The start date of the habit event to add
     * @param comment (String): The comment of the habit event to add
     * @param pictureUri (String): The URL of the picture of the habit event to add
     */
    public void addHabitEvent(Date startDate, String comment, Uri pictureUri) {
        // Create habit event and add it to the list
        HabitEvent eventToAdd = new HabitEvent(startDate, comment, pictureUri);
        _habitEvents.add(eventToAdd);

        // TODO: Add habit event to Database
    }

    /**
     * Adds a habit event to the habit event list
     * @param eventToAdd (HabitEvent): The event to add to the habit event list
     */
    public void addHabitEvent(HabitEvent eventToAdd) {
        // Add event to list
        _habitEvents.add(eventToAdd);

        // TODO: Add habit event to Database
    }
}
