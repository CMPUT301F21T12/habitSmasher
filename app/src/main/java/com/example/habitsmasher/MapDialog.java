package com.example.habitsmasher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
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
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;

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
 */
public class MapDialog extends DialogFragment implements OnMapReadyCallback {
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final String TAG = "MAP DIALOG";

    private Location _selectedLocation;
    private MapView _mapView;
    private Button _confirmButton;
    private Button _cancelButton;
    private HabitEventDialog _habitEventDialog;
    private FusedLocationProviderClient _fusedLocationClient;

    public MapDialog(Location location) {
        _selectedLocation = location;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_dialog, container, false);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        if (_selectedLocation == null) {
            @SuppressLint("MissingPermission") Task<Location> locationTask = _fusedLocationClient
                    .getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null);
            // problem: location task never completes for some reason
            while (!locationTask.isComplete());
            Location location = locationTask.getResult();
            if (location == null) {
               // _habitEventDialog.displayErrorMessage();
                getDialog().dismiss();
            }
            _selectedLocation = location;
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

        _mapView.onCreate(mapViewBundle);
        _mapView.getMapAsync(this);
        return view;
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
        // TO DO: navigate map to current/selection location
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(googleMap.getMaxZoomLevel()));
        LatLng coord = new LatLng(_selectedLocation.getLatitude(), _selectedLocation.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(coord));
        googleMap.addMarker(new MarkerOptions().position(coord).title("Habit Event Location"));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                // select location
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(latLng).title("Habit Event Location"));
                _selectedLocation.setLatitude(latLng.latitude);
                _selectedLocation.setLongitude(latLng.longitude);
            }
        });
    }
}
