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
import androidx.fragment.app.DialogFragment;

import com.example.habitsmasher.DaysTracker;
import com.example.habitsmasher.DatePickerDialogFragment;
import com.example.habitsmasher.Habit;
import com.example.habitsmasher.R;


public class EditHabitFragment extends DialogFragment{
    private EditText _titleText;
    private EditText _reasonText;
    private TextView _dateText;
    private DaysTracker _tracker;

    private final Habit _editHabit;
    private final int _index;
    private final HabitListFragment _listener;
    // viewHolder of edited Habit
    private final HabitItemAdapter.HabitViewHolder _viewHolder;

    private Button _mondayButton;
    private Button _tuesdayButton;
    private Button _wednesdayButton;
    private Button _thursdayButton;
    private Button _fridayButton;
    private Button _saturdayButton;
    private Button _sundayButton;


    public EditHabitFragment(int index, Habit editHabit, HabitListFragment listener,
                                HabitItemAdapter.HabitViewHolder habitViewHolder) {
        _index = index;
        _editHabit = editHabit;
        _listener = listener;
        _viewHolder = habitViewHolder;
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
        _titleText = view.findViewById(R.id.habit_title_edit_text);
        _reasonText = view.findViewById(R.id.habit_reason_edit_text);
        _dateText = view.findViewById(R.id.habit_date_selection);
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
        _titleText.setText(_editHabit.getTitle());
        _reasonText.setText(_editHabit.getReason());
        _dateText.setText(DatePickerDialogFragment.parseDateToString(_editHabit.getDate()));

        _dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog();
            }
        });

        // when ok is clicked
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HashMap<String, Habit> habitData = new HashMap<>();
                String habitTitle = _titleText.getText().toString();
                String reasonText = _reasonText.getText().toString();
                String dateText = _dateText.getText().toString();


                HabitValidator habitValidator = new HabitValidator(getActivity());

                if (!habitValidator.isHabitValid(habitTitle, reasonText, dateText, _tracker)) {
                    return;
                }

                // update local list and display
                _listener.updateAfterEdit(habitTitle, reasonText, DatePickerDialogFragment.parseStringToDate(dateText), _index, _tracker, _viewHolder);


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
        setListenersForDaysOfTheWeek();

        return view;
    }

    /*
     * Sets up the buttons for the days of the week, including setting the initial states.
     */
    private void setListenersForDaysOfTheWeek(){

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
