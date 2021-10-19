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

    public void addHabit(String title, String reason, Date date) {
        Habit newHabit = new Habit(title, reason, date);
        _habits.add(newHabit);
        //addHabitToDatabase(title, reason, date);
    }
    public void addHabit(Habit habit) {
        _habits.add(habit);
        //addHabitToDatabase(title, reason, date);
    }

    /**
     * Method that edits the habit at position pos
     * @param title New title of habit
     * @param reason New reason of habit
     * @param date New date of habit
     * @param pos Position of habit in the HabitList
     */
    public void editHabit(String title, String reason, Date date, int pos) {
        try {
            Habit habit = _habits.get(pos);
            habit.setTitle(title);
            habit.setReason(reason);
            habit.setDate(date);
        } catch (Exception e) {
            return;
        }
    }


}