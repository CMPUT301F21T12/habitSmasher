package com.example.habitsmasher;
import static android.content.ContentValues.TAG;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;

public class ProgressTracker {
    private HabitEventList _events;
    private static final String PATTERN = "dd/MM/yyyy";


    /*public HabitEventList removeDuplicateDays(HabitEventList events) {

        HabitEventList duplicate = new HabitEventList();
        duplicate.getHabitEvents().addAll(events.getHabitEvents());

        for (int i = 0; i < duplicate.getHabitEvents().size() - 1; i++) {
            HabitEvent current = duplicate.getHabitEvents().get(i);
            HabitEvent next = duplicate.getHabitEvents().get(i + 1);

            if (current.getDate().toString().equals(next.getDate().toString())) {
                Log.d(TAG, "REMOVING!");
                duplicate.getHabitEvents().remove(next);
            }
        }

        return duplicate;
    }*/

    public HabitEventList removeDuplicateDays(HabitEventList events) {
        HabitEventList duplicate = new HabitEventList();

        if (events.getHabitEvents().size() <= 1) {
            return events;
        }

        // Sort list by date
        if(events.getHabitEvents().size() > 1) {
            Collections.sort(events.getHabitEvents(), new Comparator<HabitEvent>() {
                public int compare(HabitEvent o1, HabitEvent o2) {
                    return o1.getDate().compareTo(o2.getDate());
                }
            });
        }

        HabitEvent current = events.getHabitEvents().get(0);
        duplicate.getHabitEvents().add(current);

        for (int i = 1; i < events.getHabitEvents().size(); i++) {
            if (!(events.getHabitEvents().get(i).getDate().toString().equals(current.getDate().toString()))) {
                current = events.getHabitEvents().get(i);
                Log.d(TAG, "[ " + current.getDate().toString());
                duplicate.getHabitEvents().add(current);
            }
        }

        return duplicate;
    }
}
