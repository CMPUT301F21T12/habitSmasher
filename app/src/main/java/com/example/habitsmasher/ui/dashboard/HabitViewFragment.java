package com.example.habitsmasher.ui.dashboard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.habitsmasher.DaysTracker;
import com.example.habitsmasher.Habit;
import com.example.habitsmasher.R;

import java.text.SimpleDateFormat;

/**
 * This class handles the Habit View Fragment
 */
public class HabitViewFragment extends Fragment {

    private Habit _habit;

    public HabitViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get passed in habit
        if(getArguments() != null) {
            _habit = (Habit) getArguments().getSerializable("habit");
        }

        //set the DaysTracker
        DaysTracker tracker = new DaysTracker(_habit.getDays());

        // Date formatter
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_habit_view, container, false);

        // Grab text boxes
        TextView descriptionHabitTextBox = view.findViewById(R.id.descriptionHabitTextBox);
        TextView dateHabitTextBox = view.findViewById(R.id.dateHabitTextBox);

        // Setting title of fragment to habit title
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(_habit.getTitle());

        // Setting text boxes
        descriptionHabitTextBox.setText(_habit.getReason());
        dateHabitTextBox.setText(String.format(dateHabitTextBox.getText().toString(), simpleDateFormat.format(_habit.getDate())));

        //all of the non-clickable buttons
        Button mondayButton = view.findViewById(R.id.monday_button);
        if (tracker.getMonday()){
            mondayButton.performClick();
        }
        Button tuesdayButton = view.findViewById(R.id.tuesday_button);
        if (tracker.getTuesday()){
            tuesdayButton.performClick();
        }
        Button wednesdayButton = view.findViewById(R.id.wednesday_button);
        if (tracker.getWednesday()){
            wednesdayButton.performClick();
        }
        Button thursdayButton = view.findViewById(R.id.thursday_button);
        if (tracker.getThursday()){
            thursdayButton.performClick();
        }
        Button fridayButton = view.findViewById(R.id.friday_button);
        if (tracker.getFriday()){
            fridayButton.performClick();
        }
        Button saturdayButton = view.findViewById(R.id.saturday_button);
        if (tracker.getSaturday()){
            saturdayButton.performClick();
        }
        Button sundayButton = view.findViewById(R.id.sunday_button);
        if (tracker.getSunday()){
            sundayButton.performClick();
        }

        return view;
    }
}