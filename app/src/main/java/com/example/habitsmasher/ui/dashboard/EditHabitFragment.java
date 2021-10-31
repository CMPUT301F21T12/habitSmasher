package com.example.habitsmasher.ui.dashboard;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.habitsmasher.DaysTracker;
import com.example.habitsmasher.Habit;
import com.example.habitsmasher.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class EditHabitFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private static final String HEADER = "Edit Dialog";
    private static final String PATTERN = "dd/MM/yyyy";
    private EditText _titleText;
    private EditText _reasonText;
    private TextView _dateText;
    private DaysTracker _tracker;

    private final Habit _editHabit;
    private final int _index;
    private final HabitListFragment _listener;
    // viewHolder of edited Habit
    private final HabitItemAdapter.HabitViewHolder _viewHolder;

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
        Button mondayButton = view.findViewById(R.id.monday_button);
        Button tuesdayButton = view.findViewById(R.id.tuesday_button);
        Button wednesdayButton = view.findViewById(R.id.wednesday_button);
        Button thursdayButton = view.findViewById(R.id.thursday_button);
        Button fridayButton = view.findViewById(R.id.friday_button);
        Button saturdayButton = view.findViewById(R.id.saturday_button);
        Button sundayButton =view.findViewById(R.id.sunday_button);

        header.setText(HEADER);

        // presetting text to values of habit
        _titleText.setText(_editHabit.getTitle());
        _reasonText.setText(_editHabit.getReason());
        _dateText.setText(parseDateToString(_editHabit.getDate()));

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
                _listener.updateAfterEdit(habitTitle, reasonText, parseStringToDate(dateText), _index, _tracker, _viewHolder);

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

        //setting day initial states (clicked or not)
        if (_tracker.getMonday()){mondayButton.performClick();}
        if (_tracker.getTuesday()){tuesdayButton.performClick();}
        if (_tracker.getWednesday()){wednesdayButton.performClick();}
        if (_tracker.getThursday()){thursdayButton.performClick();}
        if (_tracker.getFriday()){fridayButton.performClick();}
        if (_tracker.getSaturday()){saturdayButton.performClick();}
        if (_tracker.getSunday()){sundayButton.performClick();}

        //day button onClick methods follow below
        mondayButton.setOnClickListener(new View.OnClickListener() {
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

        tuesdayButton.setOnClickListener(new View.OnClickListener() {
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

        wednesdayButton.setOnClickListener(new View.OnClickListener() {
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

        thursdayButton.setOnClickListener(new View.OnClickListener() {
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

        fridayButton.setOnClickListener(new View.OnClickListener() {
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

        saturdayButton.setOnClickListener(new View.OnClickListener() {
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

        sundayButton.setOnClickListener(new View.OnClickListener() {
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


        return view;
    }

    /**
     * Opens the calendar dialog used for date selection
     */
    private void openDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    /**
     * Sets the text of the date select view to reflect selected date
     * @param view
     * @param year year of selected date
     * @param month month of selected date (integer from 0 to 11)
     * @param day day of month of selected date
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //1 is added to the month we get from the DatePickerDialog
        // because DatePickerDialog returns values between 0 and 11,
        // which is not really helpful for users.
        int correctedMonth = month + 1;
        String date = day + "/" + correctedMonth + "/" + year;
        _dateText.setText(date);
    }

    /**
     * Takes in a Date object and converts it into a string representation
     * of the date
     * @param date Date object
     * @return string representation of the date
     */
    private String parseDateToString(Date date) {
        DateFormat dateFormatter = new SimpleDateFormat(PATTERN);
        return dateFormatter.format(date);
    }

    /**
     * Parses a string and returns a Date Object reflecting the date
     * represented by the string, or null if an invalid string input
     * @param string String to be parsed
     * @return date object of date represented in string
     */
    private Date parseStringToDate(String string) {
        DateFormat dateFormatter = new SimpleDateFormat(PATTERN);
        dateFormatter.setLenient(false);
        try {
            return dateFormatter.parse(string);
        } catch(Exception e) {
            return null;
        }
    }
}
