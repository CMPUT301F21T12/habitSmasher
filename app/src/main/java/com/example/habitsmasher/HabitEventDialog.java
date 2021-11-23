package com.example.habitsmasher;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.habitsmasher.listeners.ClickListenerForCancel;
import com.example.habitsmasher.listeners.ClickListenerForDatePicker;
import com.example.habitsmasher.ui.history.HabitEventListFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * Abstract UI class that describes any dialog
 * involving adding and editing Habit Events
 *
 * Requesting location permissions and location code were sourced from
 * https://developer.android.com/training/location
 * https://developer.android.com/training/permissions/requesting
 *
 * @author Jason Kim
 */
public abstract class HabitEventDialog extends DialogFragment implements DisplaysErrorMessages {

    // codes for the different error messages that are displayed by the habit event dialog
    public static final int INCORRECT_COMMENT = 1;
    public static final int INCORRECT_DATE = 2;
    public static final int LOCATION_NOT_RECORDED = 3;

    // fragment that spawned this habit event dialog
    protected HabitEventListFragment _habitEventListFragment;

    // tag for logging
    protected String TAG;

    // selected image (currently not in use until images are implemented)
    protected Uri _selectedImage;

    // text elements in habit event dialog
    protected EditText _eventCommentText;
    protected TextView _eventDateText;
    protected TextView _errorText;
    protected TextView _header;

    // view of image
    protected ImageView _eventPictureView;

    // confirm and cancel buttons
    protected Button _confirmButton;
    protected Button _cancelButton;

    //add location button
    protected FloatingActionButton _addLocationButton;

    // location header
    protected TextView _locationHeader;

    // location of habit event in the form of a string
    protected String _selectedLocation = "";

    private ActivityResultLauncher<String[]> _requestPermissionsLauncher;

    /**
     * Initializes the variables holding the UI elements
     * of the habit event dialog
     * @param view view representing the habit event dialog
     */
    protected void initializeUIElements(View view) {
        setUpLocationPermissions();
        _eventCommentText = view.findViewById(R.id.add_habit_event_comment);
        _eventDateText = view.findViewById(R.id.habit_event_date_selection);
        _errorText = view.findViewById(R.id.error_text_event);
        _header = view.findViewById(R.id.add_habit_event_header);
        _eventPictureView = view.findViewById(R.id.habit_event_add_photo);
        _confirmButton = view.findViewById(R.id.confirm_habit_event);
        _cancelButton = view.findViewById(R.id.cancel_habit_event);
        _addLocationButton = view.findViewById(R.id.add_location_button);
        _locationHeader = view.findViewById(R.id.add_location_label);
    }

    /**
     * Sets up the listener for the date text view
     */
    protected void setDateTextViewListener() {
        _eventDateText.setOnClickListener(new ClickListenerForDatePicker(getFragmentManager(),
                                              _eventDateText));
    }

    /**
     * Defines the logic when the cancel button is clicked
     */
    protected void setCancelButtonListener() {
        _cancelButton.setOnClickListener(new ClickListenerForCancel(getDialog(), TAG));
    }

    /**
     * Defines the logic when the confirm button is clicked
     */
    protected abstract void setConfirmButtonListener();

    /**
     *
     */
    protected void setLocationButtonListener() {
        _addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "clicked");
                int coarsePermission = ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION);
                int precisePermission = ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (coarsePermission == PackageManager.PERMISSION_GRANTED &&
                        precisePermission == PackageManager.PERMISSION_GRANTED) {
                    // do location stuff
                    handleLocationSelection();
                }
                else {
                    String[] permissionsArray = {Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION};
                    _requestPermissionsLauncher.launch(permissionsArray);
                }
            }
        });
    }

    protected void handleLocationSelection() {
        MapDialog mapDialog = new MapDialog(_selectedLocation);
        mapDialog.setTargetFragment(this, 1);
        mapDialog.show(getFragmentManager(), "MapDialog");
    }

    public void selectLocation(String location) {
        _selectedLocation = location;
        if (!_selectedLocation.equals("")) {
            _locationHeader.setText("EDIT LOCATION");
        }
    }

    /**
     * Opens the calendar dialog used for date selection
     */
    protected void openDatePickerDialog() {
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
                _eventDateText.setText(date);
            }
        });
        datePickerDialogFragment.show(getFragmentManager(), "DatePickerDialogFragment");
    }

    @Override
    public void displayErrorMessage(int messageType) {
        switch(messageType) {
            case INCORRECT_COMMENT:
                /* even though the comment field is an EditText, we use errorText to
                display the error for aesthetic purposes */
                _errorText.setText("Incorrect habit event comment entered");
                break;
            case INCORRECT_DATE:
                _errorText.setText("Please enter a start date");
                break;
            case LOCATION_NOT_RECORDED:
                _errorText.setText("Location not recorded");
        }
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

    private void setUpLocationPermissions(){
        _requestPermissionsLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                isGranted -> {
                    if (isGranted.get(Manifest.permission.ACCESS_COARSE_LOCATION) &&
                            isGranted.get(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // do location stuff
                        handleLocationSelection();
                    }
                    else {
                        // denied location permissions
                        Log.d(TAG, "Location permission denied");
                    }
                });
    }
}
