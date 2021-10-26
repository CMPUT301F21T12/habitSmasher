package com.example.habitsmasher.ui.dashboard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.R;
import com.example.habitsmasher.User;
import com.example.habitsmasher.ui.history.HabitEventItemAdapter;
import com.example.habitsmasher.ui.history.HabitEventListFragment;

import java.text.SimpleDateFormat;

/**
 * This class handles the Habit View Fragment
 */
public class HabitViewFragment extends Fragment {

    private Habit _habit;
    private final User _user = new User("TestUser", "123");

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

        // Get history button and add listener
        AppCompatButton historyButton = view.findViewById(R.id.eventHistoryButton);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHabitEventsView();
            }
        });

        return view;
    }

    private void openHabitEventsView() {
        // Create Habit Event List view with needed parameters
        HabitEventListFragment fragment = HabitEventListFragment.newInstance(_habit, _user);

        // Replace current fragment with habit view
        FragmentTransaction transaction = this.getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, fragment);
        transaction.addToBackStack(null);

        // Load new fragment
        transaction.commit();
    }
}