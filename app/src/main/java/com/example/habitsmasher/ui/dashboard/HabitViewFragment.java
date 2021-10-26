package com.example.habitsmasher.ui.dashboard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        return view;
    }
}