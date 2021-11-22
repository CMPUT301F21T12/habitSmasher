package com.example.habitsmasher.ui.history;

import android.app.DatePickerDialog;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.habitsmasher.DatePickerDialogFragment;
import com.example.habitsmasher.HabitEvent;
import com.example.habitsmasher.HabitEventDialog;
import com.example.habitsmasher.MapDialog;
import com.example.habitsmasher.R;

import java.util.Date;

/**
 * The EditHabitEventFragment class
 * Based on EditEventFragment, dialog which pops up and allows a user to edit habit events
 *
 *  Requesting location permissions and location code were sourced from
 *  https://developer.android.com/training/location
 */
public class EditHabitEventDialog extends HabitEventDialog {
    // Initialize global variables
    private final int _index;
    private final HabitEvent _editHabitEvent;
    private final EditHabitEventDialog _editFragment = this;

    /**
     * Default constructor
     * @param index (int) The index of the habit to edit within the list
     * @param editHabitEvent (HabitEvent) The habit event to edit
     */
    public EditHabitEventDialog(int index, HabitEvent editHabitEvent) {
        _index = index;
        _editHabitEvent = editHabitEvent;

        // tag for logging
        TAG = "EditHabitEventDialog";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Used same UI xml as Add Habit Event, since the dialog box is the same
        View view = inflater.inflate(R.layout.add_habit_event_dialog, container, false);
        // Connect UI elements
        initializeUIElements(view);

        // set header
        _header.setText("Edit Habit Event");

        //set error text to blank
        _errorText.setText("");

        // Add listener to date text to open date picker
        setDateTextViewListener();

        // Add listener to confirm button that propagates habit event editing
        setConfirmButtonListener();

        // Add listener to cancel button that closes the dialog
        setCancelButtonListener();

        // Add listener for location button
        setLocationButtonListener();

        // Prefill values
        _eventCommentText.setText(_editHabitEvent.getComment());
        _eventDateText.setText(DatePickerDialogFragment.parseDateToString(_editHabitEvent.getDate()));

        _selectedLocation = _editHabitEvent.getLocation();

        return view;
    }

    protected void handleLocationSelection() {
        MapDialog mapDialog = new MapDialog(_selectedLocation);
        mapDialog.setTargetFragment(this, 1);
        mapDialog.show(getFragmentManager(), "MapDialog");
    }

    @Override
    protected void setConfirmButtonListener() {
        _confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get updated comment and date
                String eventComment = _eventCommentText.getText().toString();
                String dateText = _eventDateText.getText().toString();

                // Validate that the comment and date are valid
                HabitEventValidator eventValidator = new HabitEventValidator(_editFragment);
                if (!eventValidator.isHabitEventValid(eventComment, dateText)) {
                    return;
                }

                // Update the habit event in the database and locally
                Date newDate = DatePickerDialogFragment.parseStringToDate(dateText);
                HabitEvent editedHabitEvent;
                if (_selectedLocation == null) {
                     editedHabitEvent = new HabitEvent(newDate,
                            eventComment,
                            _editHabitEvent.getId());

                }
                else {
                    editedHabitEvent = new HabitEvent(newDate,
                            eventComment,
                            _editHabitEvent.getId(),
                            _selectedLocation);
                }
                _errorText.setText("");
                _habitEventListFragment.updateListAfterEdit(editedHabitEvent,_index);

                // Close dialog
                getDialog().dismiss();
            }
        });
    }
}
