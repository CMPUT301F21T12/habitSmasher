package com.example.habitsmasher.ui.dashboard;

import static org.junit.Assert.*;

import com.example.habitsmasher.DatabaseEntity;
import com.example.habitsmasher.DaysTracker;
import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitEventList;
import com.example.habitsmasher.HabitList;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class HabitListTest {
    private HabitEventList EMPTY_HABIT_EVENTS_LIST = new HabitEventList();
    private HabitList _habitList;
    private static final String SAMPLE_DAYS_OF_THE_WEEK = "MO WE FR";
    private static final boolean PUBLIC_HABIT = true;

    @Before
    public void setUp(){
        _habitList = new HabitList();
    }

    @Test
    public void addHabit_validHabitAddition_expectHabitAddedToList() {
        String habitId = DatabaseEntity.generateId();
        Habit habit = new Habit("Title 1", "Reason 1", new Date(), SAMPLE_DAYS_OF_THE_WEEK, true, habitId, EMPTY_HABIT_EVENTS_LIST);

        _habitList.addHabitLocal(habit);

        assertEquals(1, _habitList.getHabitList().size());
        assertTrue(_habitList.getHabitList().contains(habit));
    }

    @Test
    public void editHabit_validEdit_expectHabitToBeEdited() {
        String habitId = DatabaseEntity.generateId();

        Habit habit = new Habit("Title 1", "Reason 1", new Date(), "MO", PUBLIC_HABIT,  habitId, EMPTY_HABIT_EVENTS_LIST);
        _habitList.addHabitLocal(habit);
        Date newDate = new Date();
        int habitToEdit = 0;
        String newTitle = "Title 2";
        String newReason = "Reason 2";
        DaysTracker tracker = new DaysTracker(SAMPLE_DAYS_OF_THE_WEEK);
        Habit habitEdit = new Habit(newTitle, newReason, newDate, tracker.getDays(), PUBLIC_HABIT, DatabaseEntity.generateId(), EMPTY_HABIT_EVENTS_LIST);
        _habitList.editHabitLocal(habitEdit, habitToEdit);
        Habit editedHabit = _habitList.getHabitList().get(habitToEdit);

        assertEquals(newTitle, editedHabit.getTitle());
        assertEquals(newReason, editedHabit.getReason());
        assertEquals(newDate, editedHabit.getDate());
        assertEquals(habitId, editedHabit.getId());
        assertEquals(tracker.getListWithStrings(),editedHabit.getDays());
    }

    @Test
    public void deleteHabitLocally_withValidHabitPosition_expectHabitDeletedFromList() {
        Date today = new Date();
        ArrayList<Habit> localHabitList = _habitList.getHabitList();

        Habit habitToDelete = new Habit("Habit 2", "Reason 2", today, SAMPLE_DAYS_OF_THE_WEEK,
                                        PUBLIC_HABIT, DatabaseEntity.generateId(), EMPTY_HABIT_EVENTS_LIST);
        _habitList.addHabitLocal(new Habit("Habit 1", "Reason 1", today,SAMPLE_DAYS_OF_THE_WEEK,
                                PUBLIC_HABIT, DatabaseEntity.generateId(), EMPTY_HABIT_EVENTS_LIST));
        _habitList.addHabitLocal(habitToDelete);
        _habitList.addHabitLocal(new Habit("Habit 3", "Reason 3", today, SAMPLE_DAYS_OF_THE_WEEK,
                                PUBLIC_HABIT, DatabaseEntity.generateId(), EMPTY_HABIT_EVENTS_LIST));

        assertEquals(3, localHabitList.size());
        int deleteIndex = 1;
        assertTrue(localHabitList.contains(habitToDelete));

        // delete Habit
        _habitList.deleteHabitLocal(deleteIndex);

        assertFalse(localHabitList.contains(habitToDelete));
        assertEquals(2, localHabitList.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void deleteHabitLocally_withInvalidHabitPosition_expectExceptionThrown() {
        Date today = new Date();

        _habitList.addHabitLocal(new Habit("Habit 1", "Reason 1", today, SAMPLE_DAYS_OF_THE_WEEK,
                                PUBLIC_HABIT, DatabaseEntity.generateId(), EMPTY_HABIT_EVENTS_LIST));
        _habitList.addHabitLocal(new Habit("Habit 2", "Reason 2", today, SAMPLE_DAYS_OF_THE_WEEK,
                                PUBLIC_HABIT, DatabaseEntity.generateId(), EMPTY_HABIT_EVENTS_LIST));

        // attempt to delete habit with invalid position
        _habitList.deleteHabitLocal(20);
    }
}