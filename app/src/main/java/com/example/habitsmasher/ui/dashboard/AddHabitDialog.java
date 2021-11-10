package com.example.habitsmasher.ui.dashboard;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.example.habitsmasher.DatePickerDialogFragment;
import com.example.habitsmasher.DaysTracker;
import com.example.habitsmasher.HabitDialog;
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
 * @author Jacob Nyugen
 * @version 1.0
 */
public class AddHabitDialog extends HabitDialog {

    private static final String TAG = "AddHabitDialog";
    private HabitListFragment _habitListFragment;
    private final AddHabitDialog _addFragment = this;


    // buttons for the days of the week
    private Button _mondayButton;
    private Button _tuesdayButton;
    private Button _wednesdayButton;
    private Button _thursdayButton;
    private Button _fridayButton;
    private Button _saturdayButton;
    private Button _sundayButton;

    private DaysTracker _tracker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_habit_dialog_box, container, false);

        // grab all the editTexts and buttons
        _habitTitleEditText = view.findViewById(R.id.habit_title_edit_text);
        _habitReasonEditText = view.findViewById(R.id.habit_reason_edit_text);
        _habitDateTextView = view.findViewById(R.id.habit_date_selection);
        _errorText = view.findViewById(R.id.error_text);
        Button confirmNewHabit = view.findViewById(R.id.confirm_habit);
        Button cancelNewHabit = view.findViewById(R.id.cancel_habit);

        //buttons for the days of the week, apologies for so many of them
        _mondayButton = view.findViewById(R.id.monday_button);
        _tuesdayButton = view.findViewById(R.id.tuesday_button);
        _wednesdayButton = view.findViewById(R.id.wednesday_button);
        _thursdayButton = view.findViewById(R.id.thursday_button);
        _fridayButton = view.findViewById(R.id.friday_button);
        _saturdayButton = view.findViewById(R.id.saturday_button);
        _sundayButton =view.findViewById(R.id.sunday_button);

        //logic handler for tracking all those days
        _tracker = new DaysTracker();

        // set the listeners for the days of the week buttons
        setListenersForDaysOfTheWeek();

        // set the listener for the date picker
        _habitDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog();
            }
        });

        // cancel button logic
        cancelNewHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Cancel");
                getDialog().dismiss();
            }
        });

        // confirm button logic
        confirmNewHabit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Confirm");

                // validator for the habit, ensures everything is valid
                HabitValidator habitValidator = new HabitValidator(_addFragment);

                String habitTitle = _habitTitleEditText.getText().toString();
                String habitReason = _habitReasonEditText.getText().toString();
                String habitDate = _habitDateTextView.getText().toString();

                // if the habit is valid, add it to the local list and external db
                if (habitValidator.isHabitValid(habitTitle,
                                                habitReason,
                                                habitDate, _tracker)){
                    _habitListFragment.addHabitToDatabase(habitTitle,
                                                          habitReason,
                                                          habitValidator.checkHabitDateValid(habitDate), _tracker);
                    getDialog().dismiss();
                }
            }
        });

        return view;
    }

    /**
     * Sets the onClickListeners for the different buttons representing the
     * different days of the week
     */
    private void setListenersForDaysOfTheWeek(){

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
     * Opens a DatePickerDialog that is used to select the date of the added habit
     */
    private void openDatePickerDialog(){
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
                String date =  day + "/" + correctedMonth + "/" + year;
                _habitDateTextView.setText(date);
            }
        });
        datePickerDialogFragment.show(getFragmentManager(), "DatePickerDialogFragment");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            _habitListFragment = (HabitListFragment) getTargetFragment();
        } catch (ClassCastException e){
            Log.e(TAG, "Exception" + e.getMessage());
        }
    }

}