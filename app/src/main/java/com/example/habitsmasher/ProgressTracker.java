package com.example.habitsmasher;

import android.util.Log;
import static android.content.ContentValues.TAG;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class ProgressTracker {
    private static final String DAY_PATTERN = "EEEE";
    private Habit _parentHabit;
    private String[] _daysIndexHelper = {"MO", "TU", "WE", "TH", "FR", "SA", "SU"};
    private HabitEventList _events;

    public ProgressTracker(Habit parentHabit, HabitEventList events) {
        _parentHabit = parentHabit;
        _events = events;
    }

    private HabitEventList removeDuplicateDays() {
        HabitEventList noDuplicates = new HabitEventList();

        // If there's one or less items in the list, there are no duplicates so return
        if (_events.getHabitEvents().size() <= 1) {
            return _events;
        }

        // If there's more than one event, sort list by date in ascending order
        if(_events.getHabitEvents().size() > 1) {
            Collections.sort(_events.getHabitEvents(), new Comparator<HabitEvent>() {
                public int compare(HabitEvent o1, HabitEvent o2) {
                    return o1.getDate().compareTo(o2.getDate());
                }
            });
        }

        // Get the first event in the list and add it to the new list
        HabitEvent current = _events.getHabitEvents().get(0);
        noDuplicates.getHabitEvents().add(current);

        // Iterate through the events list
        for (int i = 1; i < _events.getHabitEvents().size(); i++) {
            // If there's an event that has a new date, set current to that event and add it to the no duplicates list
            if (!(_events.getHabitEvents().get(i).getDate().toString().equals(current.getDate().toString()))) {
                current = _events.getHabitEvents().get(i);
                noDuplicates.getHabitEvents().add(current);
            }
        }

        return noDuplicates;
    }

    private int calculateViableDaysMet() {
        HabitEventList noDuplicates = removeDuplicateDays();
        int amountOfDays = 0;

        // Create formatter to extract day from date
        SimpleDateFormat formatter = new SimpleDateFormat(DAY_PATTERN);

        // Iterate through list of events
        for (int i = 0; i < noDuplicates.getHabitEvents().size(); i++) {
            // Get first two letters of current day
            String currentDay = formatter.format(noDuplicates.getHabitEvents().get(i).getDate()).toUpperCase().substring(0, 2);

            // Check if this day is a viable one and after the start date, if so iterate amount of viable days met
            if (_parentHabit.getDays().contains(currentDay) && !(noDuplicates.getHabitEvents().get(i).getDate().before(_parentHabit.getDate()))) {
                amountOfDays++;
            }
        }

        return amountOfDays;
    }

    private int calculateViableDaysPossible() {
        Date endDate = new Date();
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(_parentHabit.getDate());
        SimpleDateFormat formatter = new SimpleDateFormat(DAY_PATTERN);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int viableDays = 0;

        // If start date is a viable day increment
        String startDateString = formatter.format(_parentHabit.getDate()).toUpperCase().substring(0, 2);
        if (_parentHabit.getDays().contains(startDateString)){
            viableDays++;
        }

        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(_parentHabit.getDate());
        }

        // Iterate through start calender
        while (startCal.before(endCal)) {
            // If current day is a viable day, increment
            String currentDay = _daysIndexHelper[startCal.get(Calendar.DAY_OF_WEEK) - 1];
            if (_parentHabit.getDays().contains(currentDay)) {
                viableDays++;
            }
            // Move to next day
            startCal.add(Calendar.DATE, 1);
        }

        return viableDays;
    }

    public float calculateProgressPercentage() {
        // Get possible days and days met
        float possibleDays = calculateViableDaysPossible();
        float daysMet = calculateViableDaysMet();

        // If there's no possible days yet return 0
        if (possibleDays == 0) {
            return 0;
        }

        return (daysMet/possibleDays) * 100;
    }
}
