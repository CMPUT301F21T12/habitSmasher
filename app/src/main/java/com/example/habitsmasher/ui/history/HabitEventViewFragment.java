package com.example.habitsmasher.ui.history;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitEvent;
import com.example.habitsmasher.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;

public class HabitEventViewFragment extends Fragment {
    // Habit event being displayed
    private HabitEvent _habitEvent;
    private Habit _parentHabit;
    private Bitmap _habitImage;
    private String _userId;
    ImageView _eventImageView;

    // Date format
    private static final String PATTERN = "dd-MM-yyyy";

    public HabitEventViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get passed in habit event
        if(getArguments() != null) {
            _habitEvent = (HabitEvent) getArguments().getSerializable("habitEvent");
            _parentHabit = (Habit) getArguments().getSerializable("parentHabit");
            _userId = (String) getArguments().getSerializable("userId");
        }

        // Date formatter
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_habit_event_view, container, false);

        // Set header
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Habit Event");

        // Grab text boxes
        TextView eventCommentTextBox = view.findViewById(R.id.view_event_comment);
        TextView eventDateTextBox = view.findViewById(R.id.view_event_date);
        _eventImageView = view.findViewById(R.id.view_event_image);

        // Setting text boxes
        eventCommentTextBox.setText(_habitEvent.getComment());
        eventDateTextBox.setText(simpleDateFormat.format(_habitEvent.getDate()));

        // Set image
        fetchEventImageFromDB();

        return view;
    }

    private void fetchEventImageFromDB() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        StorageReference ref = storageReference.child("images/" + _userId + "/" + _parentHabit.getId() + "/" + _habitEvent.getId() + "/eventImage");

        final long ONE_MEGABYTE = 1024 * 1024;

        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                _habitImage = bm;
                _eventImageView.setImageBitmap(_habitImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed to get image");
            }
        });
    }
}
