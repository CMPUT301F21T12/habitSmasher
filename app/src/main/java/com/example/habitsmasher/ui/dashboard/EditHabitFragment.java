package com.example.habitsmasher.ui.dashboard;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.habitsmasher.DaysTracker;
import com.example.habitsmasher.DatePickerDialogFragment;
import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitDialog;
import com.example.habitsmasher.R;

import java.util.Date;

/**
 *  UI Class that represents and specifies the behaviour of the dialog
 *  that is spawned when a user wants to edit a habit in their habit list
 * @author Jason Kim
 * @version 1.0
 */
public class EditHabitFragment extends HabitDialog {
    private DaysTracker _tracker;

    private Button _mondayButton;
    private Button _tuesdayButton;
    private Button _wednesdayButton;
    private Button _thursdayButton;
    private Button _fridayButton;
    private Button _saturdayButton;
    private Button _sundayButton;

    private final Habit _editHabit;
    private final int _index;
    private final HabitListFragment _listener;
    private final EditHabitFragment _editFragment = this;

    /**
     * Constructs a Edit Habit dialog
     * @param index index of habit being edited by this dialog
     * @param editHabit habit being edited
     * @param listener habit list fragment that spawned this dialog
     */
    public EditHabitFragment(int index, Habit editHabit, HabitListFragment listener) {
        _index = index;
        _editHabit = editHabit;
        _listener = listener;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Used same UI xml as Add Habit, since the dialog box is the same
        View view = inflater.inflate(R.layout.add_habit_dialog_box, container, false);

        // Connecting elements on UI xml to variables
        TextView header = view.findViewById(R.id.add_habit_header);
        Button confirmButton = view.findViewById(R.id.confirm_habit);
        Button cancelButton = view.findViewById(R.id.cancel_habit);
        _habitTitleEditText = view.findViewById(R.id.habit_title_edit_text);
        _habitReasonEditText = view.findViewById(R.id.habit_reason_edit_text);
        _habitDateTextView = view.findViewById(R.id.habit_date_selection);
        _errorText = view.findViewById(R.id.error_text);
        _tracker = new DaysTracker(_editHabit.getDays());

        //buttons for the days of the week, apologies for so many of them
        _mondayButton = view.findViewById(R.id.monday_button);
        _tuesdayButton = view.findViewById(R.id.tuesday_button);
        _wednesdayButton = view.findViewById(R.id.wednesday_button);
        _thursdayButton = view.findViewById(R.id.thursday_button);
        _fridayButton = view.findViewById(R.id.friday_button);
        _saturdayButton = view.findViewById(R.id.saturday_button);
        _sundayButton =view.findViewById(R.id.sunday_button);

        // Get header from resource file and set it
        header.setText(getResources().getString(R.string.edit_habit));

        // presetting text to values of habit
        _habitTitleEditText.setText(_editHabit.getTitle());
        _habitReasonEditText.setText(_editHabit.getReason());
        _habitReasonEditText.setText(DatePickerDialogFragment.parseDateToString(_editHabit.getDate()));

        // listener for the date picker dialog fragment
        _dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog();
            }
        });

        // when confirm button is clicked
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String habitTitle = _titleText.getText().toString();
                String reasonText = _reasonText.getText().toString();
                String dateText = _dateText.getText().toString();

                // habit validator to ensure data is formatted appropriately
                HabitValidator habitValidator = new HabitValidator(_editFragment);

                if (!habitValidator.isHabitValid(habitTitle, reasonText, dateText, _tracker)) {
                    return;
                }
                Date newDate = DatePickerDialogFragment.parseStringToDate(dateText);
                Habit editedHabit = new Habit(habitTitle,
                                        reasonText,
                                        newDate,
                                        _tracker.getDays(),
                                        _editHabit.getId(),
                                        _editHabit.getHabitEvents());

                // update local list and display
                _listener.updateListAfterEdit(editedHabit, _index);
                getDialog().dismiss();
            }
        });

        // when cancel button is clicked
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        //set up days of the week buttons
        initializeDaysOfTheWeekButtons();

        return view;
    }

    /**
     * Sets up the buttons representing the days of the week
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

        //button onClick methods follow below
        _mondayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if monday is already selected, set it to false
                if (_tracker.getMonday()){
                    _tracker.setMonday(false);
                }
                //if monday wasn't already selected, select it
                else{
                    _tracker.setMonday(true);
                }
                Log.d("Tracker Status", _tracker.getDays());
            }
        });

        _tuesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_tracker.getTuesday()){
                    _tracker.setTuesday(false);
                }
                else{
                    _tracker.setTuesday(true);
                }
                Log.d("Tracker Status", _tracker.getDays());
            }
        });

        _wednesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_tracker.getWednesday()){
                    _tracker.setWednesday(false);
                }
                else{
                    _tracker.setWednesday(true);
                }
                Log.d("Tracker Status", _tracker.getDays());
            }
        });

        _thursdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_tracker.getThursday()){
                    _tracker.setThursday(false);
                }
                else{
                    _tracker.setThursday(true);
                }
                Log.d("Tracker Status", _tracker.getDays());
            }
        });

        _fridayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_tracker.getFriday()){
                    _tracker.setFriday(false);
                }
                else{
                    _tracker.setFriday(true);
                }
                Log.d("Tracker Status", _tracker.getDays());
            }
        });

        _saturdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_tracker.getSaturday()){
                    _tracker.setSaturday(false);
                }
                else{
                    _tracker.setSaturday(true);
                }
                Log.d("Tracker Status", _tracker.getDays());
            }
        });

        _sundayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_tracker.getSunday()){
                    _tracker.setSunday(false);
                }
                else{
                    _tracker.setSunday(true);
                }
                Log.d("Tracker Status", _tracker.getDays());
            }
        });
    }

    /**
     * Opens the calendar dialog used for date selection
     */
    private void openDatePickerDialog() {
        DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment(new DatePickerDialog.OnDateSetListener() {
            /**
             * Sets the text of the date select view to reflect selected date
             * @param view
             * @param year year of selected date
             * @param month month of selected date (integer from 0 to 11)
             * @param day day of month of selected date
             */
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                int correctedMonth = month + 1;
                String date = day + "/" + correctedMonth + "/" + year;
                _dateText.setText(date);
            }
        });
        datePickerDialogFragment.show(getFragmentManager(), "DatePickerDialogFragment");
    }


}
