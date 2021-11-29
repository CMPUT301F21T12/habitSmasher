package com.example.habitsmasher.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.habitsmasher.DaysTracker;
import com.example.habitsmasher.DatePickerDialogFragment;
import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitDialog;
import com.example.habitsmasher.PublicPrivateButtons;
import com.example.habitsmasher.R;

import java.util.Date;

/**
 *  UI Class that represents and specifies the behaviour of the dialog
 *  that is spawned when a user wants to edit a habit in their habit list
 * @author Jason Kim
 * @version 1.0
 */
public class EditHabitDialog extends HabitDialog {

    private final Habit _editHabit;
    private final int _index;
    private final EditHabitDialog _editFragment = this;

    /**
     * Constructs a Edit Habit dialog
     * @param index index of habit being edited by this dialog
     * @param editHabit habit being edited
     */
    public EditHabitDialog(int index, Habit editHabit) {
        _index = index;
        _editHabit = editHabit;

        // setting tag for logging
        TAG = "EditHabitDialog";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Used same UI xml as Add Habit, since the dialog box is the same
        View view = inflater.inflate(R.layout.add_habit_dialog_box, container, false);
        initializeUIElements(view);

        // set error text to blank
        _errorText.setText("");

        // Get header from resource file and set it
        TextView header = view.findViewById(R.id.add_habit_header);
        header.setText(getResources().getString(R.string.edit_habit));

        // Gets days of week from habit to be edited and pass into tracker
        _tracker = new DaysTracker(_editHabit.getDays());

        // setting up private and public buttons
        _publicPrivateButtons = new PublicPrivateButtons(view, _editHabit.getPublic());
        _publicPrivateButtons.setClickListeners();

        // set up days of the week buttons
        initializeDaysOfTheWeekButtons();
        setListenersForDaysOfTheWeek();

        // setting listeners for confirm and cancel buttons
        setCancelButtonListener();
        setConfirmButtonListener();

        // listener for the date picker dialog fragment
        setDateTextViewListener();

        // presetting text to values of habit
        _habitTitleEditText.setText(_editHabit.getTitle());
        _habitReasonEditText.setText(_editHabit.getReason());
        _habitDateTextView.setText(DatePickerDialogFragment.parseDateToString(_editHabit.getDate()));

        return view;
    }

    @Override
    protected void setConfirmButtonListener() {
        _confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String habitTitle = _habitTitleEditText.getText().toString();
                String reasonText = _habitReasonEditText.getText().toString();
                String dateText = _habitDateTextView.getText().toString();
                boolean isPublic = _publicPrivateButtons.isHabitPublic();

                // habit validator to ensure data is formatted appropriately
                HabitValidator habitValidator = new HabitValidator(_editFragment);

                if (!habitValidator.isHabitValid(habitTitle,
                                                reasonText,
                                                dateText,
                                                _tracker,
                                                _publicPrivateButtons)) {
                    return;
                }
                Date newDate = DatePickerDialogFragment.parseStringToDate(dateText);
                Habit editedHabit = new Habit(habitTitle,
                        reasonText,
                        newDate,
                        _tracker.getDays(),
                        isPublic,
                        _editHabit.getId(),
                        _editHabit.getHabitEvents(),
                        _editHabit.getSortIndex());

                // update local list and display
                _errorText.setText("");
                _habitListFragment.updateListAfterEdit(editedHabit, _index);
                getDialog().dismiss();
            }
        });
    }

    /**
     * Sets up the buttons representing the days of the week to
     * state of habit currently being edited
     */
    private void initializeDaysOfTheWeekButtons(){
        //setting day initial states (clicked or not)
        if (_tracker.getMonday()){_mondayButton.performClick();}
        if (_tracker.getTuesday()){_tuesdayButton.performClick();}
        if (_tracker.getWednesday()){_wednesdayButton.performClick();}
        if (_tracker.getThursday()){_thursdayButton.performClick();}
        if (_tracker.getFriday()){_fridayButton.performClick();}
        if (_tracker.getSaturday()){_saturdayButton.performClick();}
        if (_tracker.getSunday()){_sundayButton.performClick();}
    }
}
