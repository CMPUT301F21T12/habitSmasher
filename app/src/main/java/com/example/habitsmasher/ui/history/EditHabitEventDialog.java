package com.example.habitsmasher.ui.history;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitEvent;
import com.example.habitsmasher.HabitEventDialog;
import com.example.habitsmasher.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

/**
 * The EditHabitEventFragment class
 * Based on EditEventFragment, dialog which pops up and allows a user to edit habit events
 */
public class EditHabitEventDialog extends HabitEventDialog {
    // Initialize global variables
    private final int _index;
    private final HabitEvent _editHabitEvent;
    private final EditHabitEventDialog _editFragment = this;
    private final String _userId;
    private final Habit _parentHabit;
    private Bitmap _habitImage;

    /**
     * Default constructor
     * @param index (int) The index of the habit to edit within the list
     * @param editHabitEvent (HabitEvent) The habit event to edit
     */
    public EditHabitEventDialog(int index, HabitEvent editHabitEvent, String userId, Habit parentHabit) {
        _index = index;
        _editHabitEvent = editHabitEvent;
        _userId = userId;
        _parentHabit = parentHabit;

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

        // Add listener to image view (not touching this during refactoring until images are done)
        setImageViewListener();

        // Prefill values
        _eventCommentText.setText(_editHabitEvent.getComment());
        _eventDateText.setText(DatePickerDialogFragment.parseDateToString(_editHabitEvent.getDate()));

        fetchEventImageFromDB();

        return view;
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
                HabitEvent editedHabitEvent = new HabitEvent(newDate,
                        eventComment,
                        _editHabitEvent.getId());
                _errorText.setText("");
                _habitEventListFragment.editHabitEvent(editedHabitEvent,_index, _selectedImage);

                // Close dialog
                getDialog().dismiss();
            }
        });
    }

    private void fetchEventImageFromDB() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        StorageReference ref = storageReference.child("images/" + _userId + "/" + _parentHabit.getId() + "/" + _editHabitEvent.getId() + "/eventImage");

        final long ONE_MEGABYTE = 1024 * 1024;

        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                _habitImage = bm;
                _eventPictureView.setImageBitmap(_habitImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                fetchEventImageFromDB();
                Log.d(TAG, "Failed to get image");
            }
        });
    }
}
