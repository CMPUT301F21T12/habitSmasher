package com.example.habitsmasher.ui.history;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.net.Uri;

import com.example.habitsmasher.HabitEvent;
import com.example.habitsmasher.HabitEventList;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

public class HabitEventListTest {
    private HabitEventList _habitEventList;

    @Before
    public void setUp() { _habitEventList = new HabitEventList(); }

    @Test
    public void addHabitEvent_validHabitEventAddition_ExpectHabitEventAdded() {
        HabitEvent habitEvent = new HabitEvent(new Date(), "Test Comment", UUID.randomUUID().toString());

        _habitEventList.addHabitEventLocally(habitEvent);

        assertEquals(1, _habitEventList.getHabitEvents().size());
        assertTrue(_habitEventList.getHabitEvents().contains(habitEvent));
    }

    @Test
    public void addHabitEvent_validHabitEventParametersAddition_ExpectHabitEventAdded() {
        Date sampleDate = new Date();
        String sampleComment = "Valid comment";
        Uri sampleUri = Uri.EMPTY;

        _habitEventList.addHabitEventLocally(sampleDate, sampleComment, sampleUri, UUID.randomUUID().toString());

        assertEquals(1, _habitEventList.getHabitEvents().size());
        assertEquals(sampleComment, _habitEventList.getHabitEvents().get(0).getComment());
    }
}
