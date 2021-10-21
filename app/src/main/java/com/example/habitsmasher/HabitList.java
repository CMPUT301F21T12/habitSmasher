package com.example.habitsmasher;

import java.util.ArrayList;
import java.util.Date;

/**
 * The habit list class is a container for the list of habits
 */
public class HabitList extends ArrayList{
    private static final ArrayList<Habit> _habits = new ArrayList<>();

    public ArrayList<Habit> getHabitList() {
        return _habits;
    }

    public void addHabit(String title, String reason, Date date, HabitEventList habitEvents) {
        Habit newHabit = new Habit(title, reason, date, habitEvents);
        _habits.add(newHabit);
        //addHabitToDatabase(title, reason, date);
    }
    public void addHabit(Habit habit) {
        _habits.add(habit);
        //addHabitToDatabase(title, reason, date);
    }
}