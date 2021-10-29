package com.example.habitsmasher.ui.dashboard;

import static org.junit.Assert.*;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitList;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class HabitListTest {
    private HabitList _habitList;

    @Before
    public void setUp(){
        _habitList = new HabitList();
    }

    @Test
    public void addHabit_validHabitAddition_expectHabitAddedToList(){
        long habitId = 0;
        Habit habit = new Habit("Title 1", "Reason 1", new Date(), habitId);

        _habitList.addHabitLocal(habit);

        assertEquals(1, _habitList.getHabitList().size());
        assertTrue(_habitList.getHabitList().contains(habit));
    }

    @Test
    public void editHabit_validEdit_expectHabitToBeEdited() {
        long habitId = 0;
        Habit habit = new Habit("Title 1", "Reason 1", new Date(), habitId);
        _habitList.addHabitLocal(habit);
        Date newDate = new Date();
        int habitToEdit = 0;
        String newTitle = "Title 2";
        String newReason = "Reason 2";
        _habitList.editHabitLocal(newTitle, newReason, newDate, habitToEdit);
        Habit editedHabit = _habitList.getHabitList().get(habitToEdit);

        assertEquals(newTitle, editedHabit.getTitle());
        assertEquals(newReason, editedHabit.getReason());
        assertEquals(newDate, editedHabit.getDate());
        assertEquals(habitId, editedHabit.getHabitId());
    }

    @Test
    public void deleteHabitLocally_withValidHabitPosition_expectHabitDeletedFromList() {
        Date today = new Date();
        ArrayList<Habit> localHabitList = _habitList.getHabitList();

        Habit habitToDelete = new Habit("Habit 2", "Reason 2", today, 1);
        _habitList.addHabitLocal(new Habit("Habit 1", "Reason 1", today, 0));
        _habitList.addHabitLocal(habitToDelete);
        _habitList.addHabitLocal(new Habit("Habit 3", "Reason 3", today, 2));

        assertEquals(3, localHabitList.size());
        assertTrue(localHabitList.contains(habitToDelete));

        // delete Habit
        _habitList.deleteHabitLocally(Integer.parseInt(String.valueOf(habitToDelete.getHabitId())));

        assertFalse(localHabitList.contains(habitToDelete));
        assertEquals(2, localHabitList.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void deleteHabitLocally_withInvalidHabitPosition_expectExceptionThrown() {
        Date today = new Date();

        _habitList.addHabitLocal(new Habit("Habit 1", "Reason 1", today, 0));
        _habitList.addHabitLocal(new Habit("Habit 2", "Reason 2", today, 1));

        // attempt to delete habit with invalid position
        _habitList.deleteHabitLocally(20);
    }
}