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
        Date date = new Date();
        _habitList.editHabit("Title 2", "Reason 2", date, 0);

        assertEquals("Title 2", _habitList.getHabitList().get(0).getTitle());
        assertEquals("Reason 2", _habitList.getHabitList().get(0).getReason());
        assertEquals(date, _habitList.getHabitList().get(0).getDate());
    }
}