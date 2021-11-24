package com.example.habitsmasher.ui.history;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitEvent;
import com.example.habitsmasher.ImageDatabaseHelper;
import com.example.habitsmasher.R;
import java.text.SimpleDateFormat;

public class HabitEventViewFragment extends Fragment {
    // Habit event being displayed
    private HabitEvent _habitEvent;
    private Habit _parentHabit;
    private String _userId;
    ImageView _eventImageView;

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
            _parentHabit = (Habit) getArguments().getSerializable("parentHabit");
            _userId = (String) getArguments().getSerializable("userId");
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
        _eventImageView = view.findViewById(R.id.view_event_image);

        // Setting text boxes
        eventCommentTextBox.setText(_habitEvent.getComment());
        eventDateTextBox.setText(simpleDateFormat.format(_habitEvent.getDate()));

        // Set image
        ImageDatabaseHelper imageDatabaseHelper = new ImageDatabaseHelper();
        imageDatabaseHelper.fetchImagesFromDB(_eventImageView, imageDatabaseHelper.getHabitEventStorageReference(_userId, _parentHabit.getId(), _habitEvent.getId()));

        return view;
    }
}
