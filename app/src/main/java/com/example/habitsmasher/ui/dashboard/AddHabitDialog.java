package com.example.habitsmasher.ui.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.habitsmasher.DatabaseEntity;
import com.example.habitsmasher.DaysTracker;
import com.example.habitsmasher.HabitDialog;
import com.example.habitsmasher.PublicPrivateButtons;
import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitEventList;
import com.example.habitsmasher.R;


/**
 * UI Class that represents and specifies the behaviour of the dialog
 * that is spawned when a user wants to add a habit to their habit list
 *
 * Making a dialogfragment from fragment came from the following video:
 * https://www.youtube.com/watch?v=LUV_djRHSEY
 *
 * Name: Mitch Tabian
 * Video Date: December 10, 2017
 *
 * Adding a DatePickerDialog came from the following video:
 * https://www.youtube.com/watch?v=AdTzD96AhE0
 *
 * Name: Mitch Tabian
 * Video Date: March 11, 2019
 *
 * @author Jacob Nguyen
 * @version 1.0
 */
public class AddHabitDialog extends HabitDialog {

    private final AddHabitDialog _addFragment = this;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_habit_dialog_box, container, false);
        initializeUIElements(view);

        // setting tags for logging
        TAG = "AddHabitDialog";

        // set error text to blank
        _errorText.setText("");

        // setting up public and private buttons
        _publicPrivateButtons = new PublicPrivateButtons(view);
        _publicPrivateButtons.setClickListeners();

        // logic handler for tracking all those days
        _tracker = new DaysTracker();

        // set the listeners for the days of the week buttons
        setListenersForDaysOfTheWeek();

        // set listeners for the confirm and cancel buttons
        setCancelButtonListener();
        setConfirmButtonListener();

        // set the listener for the date picker
        setDateTextViewListener();

        return view;
    }


    @Override
    protected void setConfirmButtonListener() {
        _confirmButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Confirm");

                // validator for the habit, ensures everything is valid
                HabitValidator habitValidator = new HabitValidator(_addFragment);

                String habitTitle = _habitTitleEditText.getText().toString();
                String habitReason = _habitReasonEditText.getText().toString();
                String habitDate = _habitDateTextView.getText().toString();
                boolean habitPublic = _publicPrivateButtons.isHabitPublic();

                // if the habit is valid, add it to the local list and external db
                if (habitValidator.isHabitValid(habitTitle,
                                                habitReason,
                                                habitDate, _tracker, _publicPrivateButtons)){
                    Habit newHabit = new Habit(habitTitle,
                                               habitReason,
                                               habitValidator.checkHabitDateValid(habitDate),
                                               _tracker.getDays(),
                                               habitPublic,
                                               DatabaseEntity.generateId(),
                                                new HabitEventList());
                    _habitListFragment.updateListAfterAdd(newHabit);
                    getDialog().dismiss();
                }
            }
        });
    }
}