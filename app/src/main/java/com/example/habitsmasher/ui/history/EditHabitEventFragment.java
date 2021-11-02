package com.example.habitsmasher.ui.history;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.habitsmasher.DatePickerDialogFragment;
import com.example.habitsmasher.HabitEvent;
import com.example.habitsmasher.R;

/**
 * The EditHabitEventFragment class
 * Based on EditEventFragment, dialog which pops up and allows a user to edit habit events
 */
public class EditHabitEventFragment extends DialogFragment {
    // Initialize UI variables
    private EditText _commentText;
    private TextView _dateText;

    // Initialize global variables
    private final int _index;
    private final HabitEvent _editHabitEvent;
    private final HabitEventListFragment _listener;
    private final HabitEventItemAdapter.HabitEventViewHolder _viewHolder;

    /**
     * Default constructor
     * @param index (int) The index of the habit to edit within the list
     * @param editHabitEvent (HabitEvent) The habit event to edit
     * @param listener (HabitEventListFragment) The parent fragment
     * @param habitEventViewHolder (HabitEventViewHolder) The view holder associated with the habit event
     */
    public EditHabitEventFragment (int index, HabitEvent editHabitEvent, HabitEventListFragment listener, HabitEventItemAdapter.HabitEventViewHolder habitEventViewHolder) {
        _index = index;
        _editHabitEvent = editHabitEvent;
        _listener = listener;
        _viewHolder = habitEventViewHolder;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Used same UI xml as Add Habit Event, since the dialog box is the same
        View view = inflater.inflate(R.layout.add_habit_event_dialog, container, false);

        // Connect UI elements
        Button confirmButton = view.findViewById(R.id.confirm_habit_event);
        Button cancelButton = view.findViewById(R.id.cancel_habit_event);
        _commentText = view.findViewById(R.id.add_habit_event_comment);
        _dateText = view.findViewById(R.id.habit_event_date_selection);

        // Prefill values
        _commentText.setText(_editHabitEvent.getComment());
        _dateText.setText(DatePickerDialogFragment.parseDateToString(_editHabitEvent.getDate()));

        // Add listener to date text to open date picker
        _dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog();
            }
        });

        // Add listener to confirm button that propagates habit event editing
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get updated comment and date
                String eventComment = _commentText.getText().toString();
                String dateText = _dateText.getText().toString();

                // Validate that the comment and date are valid
                HabitEventValidator eventValidator = new HabitEventValidator(getActivity());
                if (!eventValidator.isHabitEventValid(eventComment, dateText)) {
                    return;
                }

                // Update the habit event in the database and locally
                _listener.updateAfterEdit(eventComment, DatePickerDialogFragment.parseStringToDate(dateText),_index,_editHabitEvent.getId(), _viewHolder);

                // Close dialog
                getDialog().dismiss();
            }
        });

        // Add listener to cancel button that closes the dialog
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    /**
     * Opens the calendar dialog used for date selection
     */
    private void openDatePickerDialog() {
        DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment(new DatePickerDialog.OnDateSetListener() {
            /**
             * Sets the text of the date select view to reflect selected date
             * @param view
             * @param year year of selected date
             * @param month month of selected date (integer from 0 to 11)
             * @param day day of month of selected date
             */
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                int correctedMonth = month + 1;
                String date = day + "/" + correctedMonth + "/" + year;
                _dateText.setText(date);
            }
        });
        datePickerDialogFragment.show(getFragmentManager(), "DatePickerDialogFragment");
    }
}
