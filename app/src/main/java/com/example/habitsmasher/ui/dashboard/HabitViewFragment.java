package com.example.habitsmasher.ui.dashboard;

import android.os.Bundle;

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
import com.example.habitsmasher.ui.history.HabitEventItemAdapter;
import com.example.habitsmasher.ui.history.HabitEventListFragment;

/**
 * This class handles the Habit View Fragment
 */
public class HabitViewFragment extends Fragment {
    private Habit _currentHabit;

    public HabitViewFragment() {
        // Required empty public constructor
    }

    public HabitViewFragment(Habit currentHabit) {
        super();
        this._currentHabit = currentHabit;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param currentHabit (Habit): The habit for which the view is inflating
     * @return A new instance of fragment HabitViewFragment.
     */
    public static HabitViewFragment newInstance(Habit currentHabit) {
        HabitViewFragment fragment = new HabitViewFragment(currentHabit);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_habit_view, container, false);

        // Get UI elements
        TextView descriptionHabitTextBox = view.findViewById(R.id.descriptionHabitTextBox);
        TextView dateHabitTextBox = view.findViewById(R.id.dateHabitTextBox);
        AppCompatButton historyButton = view.findViewById(R.id.eventHistoryButton);

        // Set text of UI elements
        descriptionHabitTextBox.setText(this._currentHabit.getReason());
        dateHabitTextBox.setText(String.format(dateHabitTextBox.getText().toString(), this._currentHabit.getDate()));

        // Add history button listener
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
        HabitEventListFragment fragment = HabitEventListFragment.newInstance(_currentHabit);

        // Replace current fragment with habit view
        FragmentTransaction transaction = this.getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, fragment);
        transaction.addToBackStack(null);

        // Load new fragment
        transaction.commit();
    }
}