package com.example.habitsmasher.ui.dashboard;

import static org.junit.Assert.*;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitList;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardFragmentTest{
    DashboardFragment _dashboardFragment = new DashboardFragment();
    SimpleDateFormat _formatter = new SimpleDateFormat("dd-MM-yyyy");
    String _dateToString = "16-10-2021";
    Date _date;

    {
        try {
            _date = _formatter.parse(_dateToString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void addHabitSuccess(){
        _dashboardFragment.addNewHabit("Habit title 1", "Habit reason 1", _date);
    }
}