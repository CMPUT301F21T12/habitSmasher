package com.example.habitsmasher;

import java.util.ArrayList;
import java.util.Date;

/**
 * The habit list class is a container for the list of habits
 * Note: this class is no longer needed with _snapshots, so will delete it if ok
 */
public class HabitList extends ArrayList{
    private static final ArrayList<Habit> _habits = new ArrayList<>();

    public ArrayList<Habit> getHabitList() {
        return _habits;
    }

    public void addHabit(String title, String reason, Date date) {

        // zero to prevent compilation errors, addHabit is never used in practice
        Habit newHabit = new Habit(title, reason, date, 0);
        _habits.add(newHabit);
    }
    public void addHabit(Habit habit) {
        _habits.add(habit);
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