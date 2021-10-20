package com.example.habitsmasher.ui.dashboard;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.habitsmasher.R;

/**
 * This class handles the Habit View Fragment
 */
public class HabitViewFragment extends Fragment {

    private static final String REASON_PARAM = "habitReason";
    private static final String DATE_PARAM = "habitDate";

    private String _habitReason;
    private String _habitDate;

    public HabitViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param habitReason Reason of habit.
     * @param habitDate Date of habit.
     * @return A new instance of fragment HabitViewFragment.
     */
    public static HabitViewFragment newInstance(String habitReason, String habitDate) {
        HabitViewFragment fragment = new HabitViewFragment();
        Bundle args = new Bundle();
        args.putString(REASON_PARAM,habitReason);
        args.putString(DATE_PARAM,habitDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Gets arguments for the created fragment that were set using newInstance()
        if (getArguments() != null) {
            _habitReason = getArguments().getString(REASON_PARAM);
            _habitDate = getArguments().getString(DATE_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_habit_view, container, false);
        TextView descriptionHabitTextBox = view.findViewById(R.id.descriptionHabitTextBox);
        TextView dateHabitTextBox = view.findViewById(R.id.dateHabitTextBox);
        descriptionHabitTextBox.setText(_habitReason);
        dateHabitTextBox.setText(String.format(dateHabitTextBox.getText().toString(), _habitDate));
        return view;
    }
}