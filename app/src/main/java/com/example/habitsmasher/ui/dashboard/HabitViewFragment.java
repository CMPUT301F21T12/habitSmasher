package com.example.habitsmasher.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.habitsmasher.DaysTracker;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.R;
import com.example.habitsmasher.User;

import java.text.SimpleDateFormat;

/**
 * UI class that represents and specifies the behaviour of the interface
 * displayed when a user is viewing the details of a certain habit
 */
public class HabitViewFragment extends Fragment {

    // habit being displayed
    private Habit _habit;

    // user which owns this habit
    private User _user;

    // buttons representing days of the week
    private Button _mondayButton;
    private Button _tuesdayButton;
    private Button _wednesdayButton;
    private Button _thursdayButton;
    private Button _fridayButton;
    private Button _saturdayButton;
    private Button _sundayButton;

    private DaysTracker _tracker;

    private static final String PATTERN = "dd-MM-yyyy";

    public HabitViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get passed in habit
        if(getArguments() != null) {
            _habit = (Habit) getArguments().getSerializable("habit");
            _user = (User) getArguments().getSerializable("user");
        }

        //set the DaysTracker
        _tracker = new DaysTracker(_habit.getDays());

        // Date formatter
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN);

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

        //set up the buttons for the days of the week
        setupDaysOfTheWeekButtons(view);

        return view;
    }

    /**
     * Used to open up the list of habit events associated with this habit
     */
    private void openHabitEventsView() {
        // Create a bundle to be passed into the HabitEventListFragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("parentHabit", _habit);
        bundle.putSerializable("parentUser", _user);

        NavController controller = NavHostFragment.findNavController(this);
        controller.navigate(R.id.action_navigation_habitView_to_habitEventListFragment, bundle);
    }


    /**
     * Helper function that sets up all of the buttons and sets their state, whether selected or not
     * The buttons are all non-clickable
     */
    private void setupDaysOfTheWeekButtons(View view){
        // For every button, if the user selected that day when adding/editing the habit,
        // Set the button status to selected
        Button mondayButton = view.findViewById(R.id.monday_button);
        if (_tracker.getMonday()){
            mondayButton.performClick();
        }
        Button tuesdayButton = view.findViewById(R.id.tuesday_button);
        if (_tracker.getTuesday()){
            tuesdayButton.performClick();
        }
        Button wednesdayButton = view.findViewById(R.id.wednesday_button);
        if (_tracker.getWednesday()){
            wednesdayButton.performClick();
        }
        Button thursdayButton = view.findViewById(R.id.thursday_button);
        if (_tracker.getThursday()){
            thursdayButton.performClick();
        }
        Button fridayButton = view.findViewById(R.id.friday_button);
        if (_tracker.getFriday()){
            fridayButton.performClick();
        }
        Button saturdayButton = view.findViewById(R.id.saturday_button);
        if (_tracker.getSaturday()){
            saturdayButton.performClick();
        }
        Button sundayButton = view.findViewById(R.id.sunday_button);
        if (_tracker.getSunday()){
            sundayButton.performClick();
        }
    }
}