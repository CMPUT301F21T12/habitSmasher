package com.example.habitsmasher;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.habitsmasher.listeners.ClickListenerForCancel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Class that spawns a map dialog that can be scrolled through
 * and used to select a location
 *
 * Embedding a map into a fragment using a mapview found here:
 * https://github.com/googlemaps/android-samples/blob/main/ApiDemos/java/app/src/gms/java/com/
 * example/mapdemo/RawMapViewDemoActivity.java
 *
 * Requesting a location came from following the tutorial here:
 * https://developer.android.com/training/location/retrieve-current
 *
 * @author Jason Kim
 */
public class MapDialog extends DialogFragment implements OnMapReadyCallback {
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final String TAG = "MAPDIALOG";

    private String _selectedLocation;
    private MapView _mapView;
    private Button _confirmButton;
    private Button _cancelButton;
    private HabitEventDialog _habitEventDialog;
    private FusedLocationProviderClient _fusedLocationClient;
    private Bundle _mapViewBundle;

    public MapDialog(String location) {
        _selectedLocation = location;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_dialog, container, false);


        _mapViewBundle = null;
        if (savedInstanceState != null) {
            _mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        _mapView = (MapView) view.findViewById(R.id.edit_map);
        _cancelButton = view.findViewById(R.id.cancel_map);
        _confirmButton = view.findViewById(R.id.confirm_map);
        _cancelButton.setOnClickListener(new ClickListenerForCancel(getDialog(), TAG));
        // send location back to habit event
        _confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                _habitEventDialog.selectLocation(_selectedLocation);
                getDialog().dismiss();
            }
        });
        _mapView.onCreate(_mapViewBundle);
        _mapView.getMapAsync(this);
        return view;
    }

    /**
     * If needed, retrieves the user's current location. Makes use of
     * Firestore callback interface to prevent issues from asynchronous execution
     * @param callback
     */
    private void getCurrentLocation(FirestoreCallback callback) {
        _fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY,
                                  null)
                             .addOnSuccessListener(location -> {
                                 if (location != null) {
                                     _selectedLocation = location.getLatitude() +
                                                         " " + location.getLongitude();
                                     callback.onCallback(false);
                                 }
                                 else {
                                     callback.onCallback(true);
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            // Get habit event fragment for later use
            _habitEventDialog = (HabitEventDialog) getTargetFragment();
        }
        catch (ClassCastException e) {
            Log.e(TAG, "Exception" + e.getMessage());
        }
        _fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (_selectedLocation.equals("")) {
            getCurrentLocation(isLocationNull -> {
                if (isLocationNull) {
                    // location is recorded as null for some reason
                    _habitEventDialog.displayErrorMessage(HabitEventDialog.LOCATION_NOT_RECORDED);
                    getDialog().dismiss();
                }
                else {
                   setUpMap(googleMap);
                }
            });
        }
        else {
            setUpMap(googleMap);
        }
    }

    /**
     * Method that setups the Google Map snippet
     * @param googleMap google map snippet
     */
    private void setUpMap(GoogleMap googleMap) {
        String[] latLngPair = _selectedLocation.split(" ");
        LatLng coordinate = new LatLng(Double.valueOf(latLngPair[0]),
                Double.valueOf(latLngPair[1]));
        googleMap.addMarker(new MarkerOptions().position(coordinate).title("Habit Event Location"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate,
                googleMap.getMaxZoomLevel()));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                // select location
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(latLng).title("Habit Event Location"));
                _selectedLocation = latLng.latitude + " " + latLng.longitude;
            }
        });
    }
}
