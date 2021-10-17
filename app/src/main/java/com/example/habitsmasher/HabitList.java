package com.example.habitsmasher;

import java.util.ArrayList;
import java.util.Date;

public class HabitList extends ArrayList{
    private static final ArrayList<Habit> _habits = new ArrayList<>();

    public ArrayList<Habit> getHabitList() {
        return _habits;
    }
}