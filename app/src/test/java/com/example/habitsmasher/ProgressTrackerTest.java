package com.example.habitsmasher;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import static android.content.ContentValues.TAG;
import static org.junit.Assert.assertEquals;


import java.util.Date;
import java.util.UUID;

public class ProgressTrackerTest {
    private ProgressTracker _progressTracker;
    private HabitEventList _events;

    @Before
    public void setUp() {
        // _progressTracker = new ProgressTracker();
        _events = new HabitEventList();
    }

    //@Test
    /*public void testRemoveDuplicates_validInput_expectDuplicatesRemoved() {
        HabitEvent event1 = new HabitEvent(new Date(), "", UUID.randomUUID().toString());
        HabitEvent event2 = new HabitEvent(new Date(), "", UUID.randomUUID().toString());
        HabitEvent event3 = new HabitEvent(new Date(), "", UUID.randomUUID().toString());

        _events.getHabitEvents().add(event1);
        _events.getHabitEvents().add(event2);
        _events.getHabitEvents().add(event3);

        assertEquals(3, _events.getHabitEvents().size());

        HabitEventList noDuplicates = _progressTracker.removeDuplicateDays(_events);

        assertEquals(1, noDuplicates.getHabitEvents().size());
    }*/

}
