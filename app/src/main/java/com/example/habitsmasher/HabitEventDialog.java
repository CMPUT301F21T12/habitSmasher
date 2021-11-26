package com.example.habitsmasher;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.habitsmasher.listeners.ClickListenerForCancel;
import com.example.habitsmasher.listeners.ClickListenerForDatePicker;
import com.example.habitsmasher.ui.history.HabitEventListFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * Abstract UI class that describes any dialog
 * involving adding and editing Habit Events
 *
 * Requesting location permissions was implemented by following the tutorial here:
 * https://developer.android.com/training/permissions/requesting
 * https://developer.android.com/training/location/permissions
 *
 * Embedding a map into a fragment using a mapview found here:
 * https://github.com/googlemaps/android-samples/blob/main/ApiDemos/java/app/src/gms/java/com/example/
 * mapdemo/RawMapViewDemoActivity.java
 *
 * @author Jason Kim
 */
public abstract class HabitEventDialog extends DialogFragment implements DisplaysErrorMessages,
                                                                         OnMapReadyCallback,
                                                                         PictureSelectionUser {

    // codes for the different error messages that are displayed by the habit event dialog
    public static final int INCORRECT_COMMENT = 1;
    public static final int INCORRECT_DATE = 2;
    public static final int LOCATION_NOT_RECORDED = 3;

    protected static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

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
    protected Button _editLocationButton;

    // location header
    protected TextView _locationHeader;

    protected MapView _mapView;
    protected Bundle _mapViewBundle;
    protected GoogleMap _googleMap;

    // selected location of habit event, empty string intially
    protected String _selectedLocation = "";

    // used to request location permissions to user
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
        _mapView = (MapView) view.findViewById(R.id.edit_dialog_map);
        _editLocationButton = view.findViewById(R.id.edit_location_button);
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
     * Adds listener to image view to allow user to select image
     */
    protected void setImageViewListener() {
        _eventPictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create add picture dialog
                AddPictureDialog addPictureDialog = new AddPictureDialog();
                addPictureDialog.setTargetFragment(HabitEventDialog.this, 1);
                addPictureDialog.show(getFragmentManager(), "AddPictureDialog");
            }
        });
    }

    /**
     * Sets up the listener for the location button
     */
    protected void setLocationButtonListener() {
        _addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_addLocationButton.getVisibility() ==  View.INVISIBLE) {
                    // do nothing
                    return;
                }
                checkLocationPermissions();
            }
        });
    }

    /**
     * Sets up the listener for the header
     */
    protected void setEditLocationButtonListener() {
        _editLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocationPermissions();
            }
        });
    }

    /**
     * Checks if location permissions are granted and prompts location selection
     * if it is
     */
    protected void checkLocationPermissions() {
        // checks location permissions
        int coarsePermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int precisePermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        // if granted already
        if (coarsePermission == PackageManager.PERMISSION_GRANTED &&
                precisePermission == PackageManager.PERMISSION_GRANTED) {
            // do location stuff
            handleLocationSelection();
        }
        else {
            // request permissions
            String[] permissionsArray = {Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION};
            _requestPermissionsLauncher.launch(permissionsArray);
        }
    }

    protected void spawnMapSnippet() {
        _mapView.onCreate(_mapViewBundle);
        _mapView.getMapAsync(this);
    }

    protected void wrapBundle(Bundle savedInstanceState) {
        _mapViewBundle = null;
        if (savedInstanceState != null) {
            _mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
    }

    /**
     * Spawns the map dialog used to select a location
     */
    protected void handleLocationSelection() {
        MapDialog mapDialog = new MapDialog(_selectedLocation);
        mapDialog.setTargetFragment(this, 1);
        mapDialog.show(getFragmentManager(), "MapDialog");
    }

    /**
     * Selects a location for the habit event in question
     * @param location location of the habit event
     */
    public void selectLocation(String location) {
        _selectedLocation = location;

        // changing header above location button to "EDIT LOCATION" after
        // location has been specified
        if (!_selectedLocation.equals("")) {
            _locationHeader.setVisibility(View.INVISIBLE);
            _editLocationButton.setVisibility(View.VISIBLE);
            setEditLocationButtonListener();
            _addLocationButton.setVisibility(View.INVISIBLE);
            _mapView.setVisibility(View.VISIBLE);
            repositionGoogleMap();
        }
    }

    protected void repositionGoogleMap() {
        String[] latLngPair = _selectedLocation.split(" ");
        LatLng coord = new LatLng(Double.valueOf(latLngPair[0]),
                Double.valueOf(latLngPair[1]));
        _googleMap.clear();
        _googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coord, _googleMap.getMaxZoomLevel()));
        _googleMap.addMarker(new MarkerOptions().position(coord).title("Habit Event Position"));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        _googleMap = googleMap;
        if (!_selectedLocation.equals("")) {
            repositionGoogleMap();
        }
    }

  ;

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

    /**
     * Handles the selected image
     * @param image (Uri) The image that the user has chosen
     */
    public void setImage(Uri image) {
        _selectedImage = image;
        _eventPictureView.setImageURI(_selectedImage);
    }

    /**
     * Function that sets up the launcher which handles location permissions
     */
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        _mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        _mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        _mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        _mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        _mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        _mapView.onLowMemory();
    }
}
