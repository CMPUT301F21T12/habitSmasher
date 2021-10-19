package com.example.habitsmasher.ui.dashboard;

import static org.junit.Assert.*;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitList;
import com.google.firebase.firestore.FirebaseFirestore;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;

public class HabitListTest {
    private HabitList _habitList;

    @Before
    public void setUp(){
        _habitList = new HabitList();
    }

    @Test
    public void addHabit_validHabitAddition_expectHabitAddedToList(){
        Habit habit = new Habit("Title 1", "Reason 1", new Date());

        _habitList.addHabit(habit);

        assertEquals(1, _habitList.getHabitList().size());
        assertTrue(_habitList.getHabitList().contains(habit));
    }

    @Test
    public void editHabit_vaildEdit_expectHabitToBeEdited() {
        Date newDate = new Date();
        int habitToEdit = 0;
        String newTitle = "Title 2";
        String newReason = "Reason 2";
        _habitList.editHabit(newTitle, newReason, newDate, habitToEdit);
        Habit editedHabit = _habitList.getHabitList().get(habitToEdit);

        assertEquals(newTitle, editedHabit.getTitle());
        assertEquals(newReason, editedHabit.getReason());
        assertEquals(newDate, editedHabit.getDate());
    }
}