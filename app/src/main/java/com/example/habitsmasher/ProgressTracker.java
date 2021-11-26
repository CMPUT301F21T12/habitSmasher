package com.example.habitsmasher;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class ProgressTracker {
    private static final String DAY_PATTERN = "EEEE";
    private Habit _parentHabit;

    public ProgressTracker(Habit parentHabit) {
        _parentHabit = parentHabit;
    }

    private HabitEventList removeDuplicateDays(HabitEventList events) {
        HabitEventList noDuplicates = new HabitEventList();

        // If there's one or less items in the list, there are no duplicates so return
        if (events.getHabitEvents().size() <= 1) {
            return events;
        }

        // If there's more than one event, sort list by date in ascending order
        if(events.getHabitEvents().size() > 1) {
            Collections.sort(events.getHabitEvents(), new Comparator<HabitEvent>() {
                public int compare(HabitEvent o1, HabitEvent o2) {
                    return o1.getDate().compareTo(o2.getDate());
                }
            });
        }

        // Get the first event in the list and add it to the new list
        HabitEvent current = events.getHabitEvents().get(0);
        noDuplicates.getHabitEvents().add(current);

        // Iterate through the events list
        for (int i = 1; i < events.getHabitEvents().size(); i++) {
            // If there's an event that has a new date, set current to that event and add it to the no duplicates list
            if (!(events.getHabitEvents().get(i).getDate().toString().equals(current.getDate().toString()))) {
                current = events.getHabitEvents().get(i);
                noDuplicates.getHabitEvents().add(current);
            }
        }

        return noDuplicates;
    }

    public int calculateViableDaysMet(HabitEventList events) {
        HabitEventList noDuplicates = removeDuplicateDays(events);
        int amountOfDays = 0;

        // Create formatter to extract day from date
        SimpleDateFormat formatter = new SimpleDateFormat(DAY_PATTERN);

        // Iterate through list of events
        for (int i = 0; i < noDuplicates.getHabitEvents().size(); i++) {
            // Get first two letters of current day
            String currentDay = formatter.format(noDuplicates.getHabitEvents().get(i).getDate()).toUpperCase().substring(0, 2);

            // Check if this day is a viable one, if so iterate amount of viable days met
            if (_parentHabit.getDays().contains(currentDay)) {
                amountOfDays++;
            }
        }

        return amountOfDays;
    }

    public int calculateViableDaysPossible() {
        Date today = new Date();

        
    }
}
