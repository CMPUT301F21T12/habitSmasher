package com.example.habitsmasher.ui.history;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.habitsmasher.R;

public class AddHabitEventDialog extends DialogFragment {
    private static final String TAG = "AddHabitEventDialog";

    private String _username;

    private EditText _eventCommentText;

    public AddHabitEventDialog(String username) {
        _username = username;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_habit_event_dialog, container, false);
        _eventCommentText = view.findViewById(R.id.habit_event_comment);


        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}
