package com.example.habitsmasher.ui.dashboard;

import static org.junit.Assert.*;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitList;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HabitListTest{

    private HabitList _habitList;

    @Before
    public void setUp(){
        _habitList = new HabitList();
    }
    @Test
    public void testAddHabit(){
        Habit habit = new Habit("Title 1", "Reason 1", new Date());

        _habitList.addHabit(habit);

        assertEquals(1, _habitList.getHabitList().size());
        assertTrue(_habitList.getHabitList().contains(habit));
    }
}