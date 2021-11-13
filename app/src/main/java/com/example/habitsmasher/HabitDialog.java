package com.example.habitsmasher;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.example.habitsmasher.ui.dashboard.HabitListFragment;


/**
 * Abstract UI class that describes any dialog that can
 * add or edit habits
 * @author Jason Kim
 */
public abstract class HabitDialog extends DialogFragment implements DisplaysErrorMessages {

    // codes for the different error messages displayed by the habit dialog
    public static final int INCORRECT_TITLE = 1;
    public static final int INCORRECT_REASON = 2;
    public static final int INCORRECT_DATE = 3;
    public static final int INCORRECT_DAYS = 4;

    // tag of dialog for logging
    protected String TAG;

    // parent habit list fragment that spawned this habit dialog
    protected HabitListFragment _habitListFragment;

    // text elements of the habit dialog
    protected EditText _habitTitleEditText;
    protected EditText _habitReasonEditText;
    protected TextView _habitDateTextView;

    // error message text views
    protected TextView _errorText;

    // buttons for days of the week on habit dialog
    protected Button _mondayButton;
    protected Button _tuesdayButton;
    protected Button _wednesdayButton;
    protected Button _thursdayButton;
    protected Button _fridayButton;
    protected Button _saturdayButton;
    protected Button _sundayButton;

    // private and public buttons
    protected PublicPrivateButtons _publicPrivateButtons;

    // confirm and cancel button for habit dialog
    protected Button _confirmButton;
    protected Button _cancelButton;

    // days tracker used to keep track of weekly schedule of habits
    protected DaysTracker _tracker;

    /**
     * Initializes the variables holding the
     * different UI elements of the habit dialog
     * @param view view representing the habit dialog
     */
    protected void initializeUIElements(View view) {
        _habitTitleEditText = view.findViewById(R.id.habit_title_edit_text);
        _habitReasonEditText = view.findViewById(R.id.habit_reason_edit_text);
        _habitDateTextView = view.findViewById(R.id.habit_date_selection);

        _errorText = view.findViewById(R.id.error_text_habit);

        //buttons for the days of the week, apologies for so many of them
        _mondayButton = view.findViewById(R.id.monday_button);
        _tuesdayButton = view.findViewById(R.id.tuesday_button);
        _wednesdayButton = view.findViewById(R.id.wednesday_button);
        _thursdayButton = view.findViewById(R.id.thursday_button);
        _fridayButton = view.findViewById(R.id.friday_button);
        _saturdayButton = view.findViewById(R.id.saturday_button);
        _sundayButton =view.findViewById(R.id.sunday_button);

        _confirmButton = view.findViewById(R.id.confirm_habit);
        _cancelButton = view.findViewById(R.id.cancel_habit);
    }

    /**
     * Sets the onClickListeners for the different buttons representing the
     * different days of the week in the habit dialog
     */
    protected void setListenersForDaysOfTheWeek(){
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
     * Sets the listener of the date text view in
     * the habit dialog
     */
    protected void setDateTextViewListener() {
        _habitDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog();
            }
        });
    }

    /**
     * Defines logic that occurs when cancel button is
     * clicked on habit dialog
     */
    protected void setCancelButtonListener() {
        _cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    /**
     * Defines logic that occurs when confirm button is
     * clicked on habit dialog
     */
    protected abstract void setConfirmButtonListener();

    /**
     * Opens the calendar dialog used for date selection
     * in the habit dialog
     */
    protected void openDatePickerDialog() {
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

    @Override
    public void displayErrorMessage(int messageType) {
        switch(messageType) {
            case INCORRECT_TITLE:
                _habitTitleEditText.setError("Incorrect habit title entered");
                _habitTitleEditText.requestFocus();
                break;
            case INCORRECT_REASON:
                _habitReasonEditText.setError("Incorrect habit reason entered");
                _habitReasonEditText.requestFocus();
                break;
            case INCORRECT_DATE:
                _errorText.setText("Please enter a start date");
                break;
            case INCORRECT_DAYS:
                _errorText.setText("Please select a weekly schedule");
                break;
        }
    }
}

