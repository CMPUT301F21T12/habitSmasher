package com.example.habitsmasher.ui.dashboard;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.habitsmasher.DaysTracker;
import com.example.habitsmasher.R;

import java.util.Calendar;

/**
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
 */
public class AddHabitDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "AddHabitDialog";

    private HabitListFragment _habitListFragment;
    private EditText _habitTitleEditText;
    private EditText _habitReasonEditText;
    private TextView _habitDateTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_habit_dialog_box, container, false);
        _habitTitleEditText = view.findViewById(R.id.habit_title_edit_text);
        _habitReasonEditText = view.findViewById(R.id.habit_reason_edit_text);
        _habitDateTextView = view.findViewById(R.id.habit_date_selection);
        Button confirmNewHabit = view.findViewById(R.id.confirm_habit);
        Button cancelNewHabit = view.findViewById(R.id.cancel_habit);

        //buttons for the days of the week, apologies for so many of them
        Button mondayButton = view.findViewById(R.id.monday_button);
        Button tuesdayButton = view.findViewById(R.id.tuesday_button);
        Button wednesdayButton = view.findViewById(R.id.wednesday_button);
        Button thursdayButton = view.findViewById(R.id.thursday_button);
        Button fridayButton = view.findViewById(R.id.friday_button);
        Button saturdayButton = view.findViewById(R.id.saturday_button);
        Button sundayButton =view.findViewById(R.id.sunday_button);

        //logic handler for tracking all those days
        DaysTracker tracker = new DaysTracker();

        _habitDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog();
            }
        });

        cancelNewHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Cancel");
                getDialog().dismiss();
            }
        });

        confirmNewHabit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Confirm");

                HabitValidator habitValidator = new HabitValidator(getActivity());

                String habitTitle = _habitTitleEditText.getText().toString();
                String habitReason = _habitReasonEditText.getText().toString();
                String habitDate = _habitDateTextView.getText().toString();

                if (habitValidator.isHabitValid(habitTitle,
                                                habitReason,
                                                habitDate,
                                                tracker)){
                    _habitListFragment.addHabitToDatabase(habitTitle,
                                                          habitReason,
                                                          habitValidator.checkHabitDateValid(habitDate),
                                                          tracker);
                    getDialog().dismiss();
                }
            }
        });

        //button onClick methods follow below

        mondayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if monday is already selected, set it to false
                if (tracker.getMonday()){
                    tracker.setMonday(false);
                }
                //if monday wasn't already selected, select it
                else{
                    tracker.setMonday(true);
                }
                Log.d("Tracker Status", tracker.getDays());
            }
        });

        tuesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tracker.getTuesday()){
                    tracker.setTuesday(false);
                }
                else{
                    tracker.setTuesday(true);
                }
                Log.d("Tracker Status", tracker.getDays());
            }
        });

        wednesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tracker.getWednesday()){
                    tracker.setWednesday(false);
                }
                else{
                    tracker.setWednesday(true);
                }
                Log.d("Tracker Status", tracker.getDays());
            }
        });

        thursdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tracker.getThursday()){
                    tracker.setThursday(false);
                }
                else{
                    tracker.setThursday(true);
                }
                Log.d("Tracker Status", tracker.getDays());
            }
        });

        fridayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tracker.getFriday()){
                    tracker.setFriday(false);
                }
                else{
                    tracker.setFriday(true);
                }
                Log.d("Tracker Status", tracker.getDays());
            }
        });

        saturdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tracker.getSaturday()){
                    tracker.setSaturday(false);
                }
                else{
                    tracker.setSaturday(true);
                }
                Log.d("Tracker Status", tracker.getDays());
            }
        });

        sundayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tracker.getSunday()){
                    tracker.setSunday(false);
                }
                else{
                    tracker.setSunday(true);
                }
                Log.d("Tracker Status", tracker.getDays());
            }
        });

        return view;
    }

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


    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //1 is added to the month we get from the DatePickerDialog
        // because DatePickerDialog returns values between 0 and 11,
        // which is not really helpful for users.
        int correctedMonth = month + 1;
        String date = day + "/" + correctedMonth + "/" + year;
        _habitDateTextView.setText(date);
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