package com.example.habitsmasher.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.habitsmasher.HabitEvent;
import com.example.habitsmasher.R;

import java.text.SimpleDateFormat;

public class HabitEventViewFragment extends Fragment {
    // Habit event being displayed
    private HabitEvent _habitEvent;

    // Date format
    private static final String PATTERN = "dd-MM-yyyy";

    public HabitEventViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get passed in habit event
        if(getArguments() != null) {
            _habitEvent = (HabitEvent) getArguments().getSerializable("habitEvent");
        }

        // Date formatter
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_habit_event_view, container, false);

        // Set header
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Habit Event");

        // Grab text boxes
        TextView eventCommentTextBox = view.findViewById(R.id.view_event_comment);
        TextView eventDateTextBox = view.findViewById(R.id.view_event_date);

        // Setting text boxes
        eventCommentTextBox.setText(_habitEvent.getComment());
        eventDateTextBox.setText(simpleDateFormat.format(_habitEvent.getDate()));

        return view;
    }
}
