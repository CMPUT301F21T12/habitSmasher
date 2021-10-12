package com.example.habitsmasher;

import java.util.ArrayList;
import java.util.Date;

public class HabitList extends ArrayList<Habit>{
    private static final ArrayList<Habit> _habits = new ArrayList<>();

    public HabitList() {
        // prefilling the habit list with dummy habits for now
        for (int i = 0; i < 10; i++) {
            _habits.add(new Habit("Habit " + i, "Reason "+ i, new Date()));
        }
    }

    public ArrayList<Habit> getHabitList() {
        return _habits;
    }
}