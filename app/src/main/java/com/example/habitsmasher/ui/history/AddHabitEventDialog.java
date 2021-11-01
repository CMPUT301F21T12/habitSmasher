package com.example.habitsmasher.ui.history;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.example.habitsmasher.HabitEvent;
import com.example.habitsmasher.R;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * The AddHabitEventDialog
 * Deals with UI and information handling of the add habit event popup
 */
public class AddHabitEventDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "AddHabitEventDialog";

    // Relevant variables
    private final String INCORRECT_COMMENT_FORMAT = "Incorrect comment entered";
    private final String DATE_FORMAT = "dd/MM/yyyy";
    private final String INCORRECT_BLANK_DATE = "Please enter a start date";
    private String _username;
    private String _eventCommentInput;
    private Date _eventDate;

    // UI elements
    private HabitEventListFragment _habitEventListFragment;
    private EditText _eventCommentText;
    private TextView _eventDateText;
    private Button _cancelNewEvent;
    private Button _confirmNewEvent;
    private ImageView _eventPictureView;
    private Uri _selectedImage;

    /**
     * Default constructor
     * @param username (String) The username of the user who is adding events
     */
    public AddHabitEventDialog(String username) {
        _username = username;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.add_habit_event_dialog, container, false);

        // Attach UI elements
        _eventCommentText = view.findViewById(R.id.add_habit_event_comment);
        _eventDateText = view.findViewById(R.id.habit_event_date_selection);
        _cancelNewEvent = view.findViewById(R.id.cancel_habit_event);
        _confirmNewEvent = view.findViewById(R.id.confirm_habit_event);
        _eventPictureView = view.findViewById(R.id.habit_event_add_photo);

        // Add listener to date text to open date picker
        _eventDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog();
            }
        });

        // Add listener to cancel button which closes dialog
        _cancelNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Cancel");
                getDialog().dismiss();
            }
        });

        // Add listener to confirm button that adds events to database and closed dialog
        _confirmNewEvent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                HabitEventValidator habitEventValidator = new HabitEventValidator(getActivity());

                String habitEventComment = _eventCommentText.getText().toString();
                String habitEventDate = _eventDateText.getText().toString();

                // Check if event data is valid
                if (habitEventValidator.isHabitEventValid(habitEventComment, habitEventDate)) {
                    // If everything is valid, add event to database, events list, and close dialog
                    HabitEvent newEvent = new HabitEvent(habitEventValidator.checkHabitDateValid(habitEventDate), habitEventComment, _selectedImage, UUID.randomUUID());
                    _habitEventListFragment.addNewHabitEvent(newEvent);
                    getDialog().dismiss();
                }

            }
        });

        // Add listener to image view
        _eventPictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open gallery to let user pick photo
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });

        return view;
    }

    /**
     * Reference: https://stackoverflow.com/questions/10165302/dialog-to-pick-image-from-gallery-or-from-camera
     * Override onActivityResult to handle when user has selected image
     * @param requestCode
     * @param resultCode
     * @param imageReturnedIntent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
            case 1:
                if (resultCode == RESULT_OK) {
                    // Set selected picture
                    _selectedImage = imageReturnedIntent.getData();
                    _eventPictureView.setImageURI(_selectedImage);
                }
                break;
        }
    }

    /**
     * Opens date picker
     */
    private void openDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    /**
     * Handles when a date is selected
     * @param view
     * @param year
     * @param month
     * @param day
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //1 is added to the month we get from the DatePickerDialog
        // because DatePickerDialog returns values between 0 and 11,
        // which is not really helpful for users.
        int correctedMonth = month + 1;
        String date = day + "/" + correctedMonth + "/" + year;
        _eventDateText.setText(date);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            // Get habit event fragment for later use
            _habitEventListFragment = (HabitEventListFragment) getTargetFragment();
        }
        catch (ClassCastException e) {
            Log.e(TAG, "Exception" + e.getMessage());
        }
    }
}
