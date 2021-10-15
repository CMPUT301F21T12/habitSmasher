package com.example.habitsmasher;

import java.util.ArrayList;
import java.util.Date;

/**
 * The habit list class is a container for the list of habits
 */
public class HabitList extends ArrayList{
    private static final ArrayList<Habit> _habits = new ArrayList<>();

    public HabitList() {
        // prefilling the habit list with dummy habits for now
        for (int i = 0; i < 10; i++) {
            _habits.add(new Habit("Habit " + i, "Reason "+ i, new Date()));
        }
    }

    /**
     * This is a getter for the habit list stored in this class
     * @return the list of habits
     */
    public ArrayList<Habit> getHabitList() {
        return _habits;
    }
}
