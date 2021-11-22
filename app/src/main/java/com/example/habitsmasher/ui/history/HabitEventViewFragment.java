package com.example.habitsmasher.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.habitsmasher.HabitEvent;
import com.example.habitsmasher.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;

/**
 * Embedding a map into a fragment using a mapview found here:
 * https://github.com/googlemaps/android-samples/blob/main/ApiDemos/java/app/src/gms/java/com/example/
 * mapdemo/RawMapViewDemoActivity.java
 */
public class HabitEventViewFragment extends Fragment implements OnMapReadyCallback {
    // Habit event being displayed
    private HabitEvent _habitEvent;

    private MapView _mapView;

    // Date format
    private static final String PATTERN = "dd-MM-yyyy";

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    public HabitEventViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        // Get passed in habit event
        if(getArguments() != null) {
            _habitEvent = (HabitEvent) getArguments().getSerializable("habitEvent");
        }

        // Date formatter
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_habit_event_view, container, false);

        if (_habitEvent.getLocation() != null) {
            _mapView = (MapView) view.findViewById(R.id.view_map);
            _mapView.onCreate(mapViewBundle);
            _mapView.getMapAsync(this);
        }

        // Set header
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Habit Event");

        // Grab text boxes
        TextView eventCommentTextBox = view.findViewById(R.id.view_event_comment);
        TextView eventDateTextBox = view.findViewById(R.id.view_event_date);

        // Setting text boxes
        eventCommentTextBox.setText(_habitEvent.getComment());
        eventDateTextBox.setText(simpleDateFormat.format(_habitEvent.getDate()));

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
    public void onResume() {
        super.onResume();
        if (_habitEvent.getLocation() != null) {
            _mapView.onResume();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (_habitEvent.getLocation() != null) {
            _mapView.onStart();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (_habitEvent.getLocation() != null) {
            _mapView.onStop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (_habitEvent.getLocation() != null) {
            _mapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (_habitEvent.getLocation() != null) {
            _mapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (_habitEvent.getLocation() != null) {
            _mapView.onLowMemory();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng coord = new LatLng(_habitEvent.getLocation().getLatitude(),
                                  _habitEvent.getLocation().getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coord, 20));
        googleMap.addMarker(new MarkerOptions().position(coord).title("Habit Event Position"));
    }
}
