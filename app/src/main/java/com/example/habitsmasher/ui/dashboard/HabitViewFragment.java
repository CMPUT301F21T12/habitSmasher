package com.example.habitsmasher.ui.dashboard;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.habitsmasher.DaysTracker;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.ProgressTracker;
import com.example.habitsmasher.PublicPrivateButtons;
import com.example.habitsmasher.R;

import java.text.SimpleDateFormat;

/**
 * UI class that represents and specifies the behaviour of the interface
 * displayed when a user is viewing the details of a certain habit
 */
public class HabitViewFragment extends Fragment {

    // habit being displayed
    private Habit _habit;

    // user which owns this habit
    private String _userId;

    private boolean _isOwner;

    private PublicPrivateButtons _publicPrivateButtons;

    private DaysTracker _tracker;

    private static final String PATTERN = "EEE, d MMM yyyy";

    public HabitViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get passed in habit
        if(getArguments() != null) {
            _habit = (Habit) getArguments().getSerializable("habit");
            _userId = (String) getArguments().getSerializable("userId");
            // Whether the habit is owned by the current user
            try {
                _isOwner = (boolean) getArguments().getSerializable("isOwner");
            } catch (NullPointerException e){
                _isOwner = true;
            }
        }

        //set the DaysTracker
        _tracker = new DaysTracker(_habit.getDays());

        // Date formatter
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_habit_view, container, false);

        // setup the public and private buttons
        _publicPrivateButtons = new PublicPrivateButtons(view, _habit.getPublic());

        // Grab text boxes
        TextView descriptionHabitTextBox = view.findViewById(R.id.descriptionHabitTextBox);
        TextView dateHabitTextBox = view.findViewById(R.id.dateHabitTextBox);

        // Setting title of fragment to habit title
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(_habit.getTitle());

        // Setting text boxes
        descriptionHabitTextBox.setText(_habit.getReason());
        dateHabitTextBox.setText(simpleDateFormat.format(_habit.getDate()));

        // Get history button and add listener
        AppCompatButton historyButton = view.findViewById(R.id.eventHistoryButton);
        if (!_isOwner){
            historyButton.setVisibility(View.INVISIBLE);
        } else {
            historyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openHabitEventsView();
                }
            });
        }

        //set up the buttons for the days of the week
        setupDaysOfTheWeekButtons(view);

        // Grab ProgressBar
        ProgressBar progressBar = view.findViewById(R.id.habit_view_progress_bar);
        TextView progressTextView = view.findViewById(R.id.habit_view_progress_text);

        // Create a new progress tracker
        ProgressTracker progressTracker = new ProgressTracker(_habit);

        // Get integer representation of progress
        int progress = (int) progressTracker.calculateProgressPercentage();

        // Set progress in circular progress and text
        progressBar.setProgress(progress);
        String progressText = Integer.toString(progress) + "%";
        progressTextView.setText(progressText);

        return view;
    }

    /**
     * Used to open up the list of habit events associated with this habit
     */
    private void openHabitEventsView() {
        // Create a bundle to be passed into the HabitEventListFragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("parentHabit", _habit);
        bundle.putSerializable("parentUser", _userId);

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