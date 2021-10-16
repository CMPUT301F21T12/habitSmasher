package com.example.habitsmasher.ui.dashboard;

import java.util.Date;

public interface HabitDialogListener {
    void addNewHabit(String habitTitleInput, String habitReasonInput, Date habitDate);
}