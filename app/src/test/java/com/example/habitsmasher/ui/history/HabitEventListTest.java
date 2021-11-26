package com.example.habitsmasher.ui.history;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

        _habitEventList.addHabitEventLocally(sampleDate, sampleComment, UUID.randomUUID().toString(), "");

        assertEquals(1, _habitEventList.getHabitEvents().size());
        assertEquals(sampleComment, _habitEventList.getHabitEvents().get(0).getComment());
    }

    @Test
    public void deleteHabitEvent_existingEvent_ExpectHabitEventDeleted() {
        HabitEvent event1 = new HabitEvent(new Date(), "Habit 1", UUID.randomUUID().toString());
        HabitEvent event2 = new HabitEvent(new Date(), "Habit 1", UUID.randomUUID().toString());
        HabitEvent event3 = new HabitEvent(new Date(), "Habit 1", UUID.randomUUID().toString());

        _habitEventList.addHabitEventLocally(event1);
        _habitEventList.addHabitEventLocally(event2);
        _habitEventList.addHabitEventLocally(event3);

        assertEquals(3, _habitEventList.getHabitEvents().size());
        assertTrue(_habitEventList.getHabitEvents().contains(event1));
        assertTrue(_habitEventList.getHabitEvents().contains(event2));
        assertTrue(_habitEventList.getHabitEvents().contains(event3));

        _habitEventList.deleteHabitEventLocally(event2);

        assertFalse(_habitEventList.getHabitEvents().contains(event2));
        assertEquals(2, _habitEventList.getHabitEvents().size());

        _habitEventList.deleteHabitEventLocally(event1);
        _habitEventList.deleteHabitEventLocally(event3);

        assertFalse(_habitEventList.getHabitEvents().contains(event1));
        assertFalse(_habitEventList.getHabitEvents().contains(event3));
        assertEquals(0, _habitEventList.getHabitEvents().size());
    }

    @Test
    public void deleteHabitEvent_nonExistentEvent_ExpectListUnchanged() {
        HabitEvent event1 = new HabitEvent(new Date(), "Habit 1", UUID.randomUUID().toString());
        HabitEvent event2 = new HabitEvent(new Date(), "Habit 1", UUID.randomUUID().toString());

        _habitEventList.addHabitEventLocally(event1);
        _habitEventList.addHabitEventLocally(event2);
        assertEquals(2, _habitEventList.getHabitEvents().size());

        _habitEventList.deleteHabitEventLocally(new HabitEvent(new Date(), "I don't exist", UUID.randomUUID().toString()));
        assertEquals(2, _habitEventList.getHabitEvents().size());
    }

    @Test
    public void editHabitEvent_validEdit_expectEventToBeEdited() {
        HabitEvent toEdit = new HabitEvent(new Date(), "Habit 1", UUID.randomUUID().toString(), "23.1 45.6");
        _habitEventList.addHabitEventLocally(toEdit);
        String editID = toEdit.getId();
        String newComment = "New comment!";
        Date newDate = new Date();
        String newLocation = "43.1 23.4";
        int editPosition = 0;

        _habitEventList.editHabitEventLocally(newComment, newDate, editPosition, newLocation);

        HabitEvent edited = _habitEventList.getHabitEvents().get(editPosition);
        assertEquals(newComment, edited.getComment());
        assertEquals(newDate, edited.getDate());
        assertEquals(editID, edited.getId());
        assertEquals(newLocation, edited.getLocation());
    }
}
