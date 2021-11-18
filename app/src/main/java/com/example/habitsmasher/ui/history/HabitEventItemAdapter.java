package com.example.habitsmasher.ui.history;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitEvent;
import com.example.habitsmasher.HabitEventList;
import com.example.habitsmasher.ItemAdapter;
import com.example.habitsmasher.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;

/**
 * The HabitEventItemAdapter class
 * Based on HabitItemAdapter, extension of FirestoreRecycler that shows habit events list with
 * live updates
 */
public class HabitEventItemAdapter extends ItemAdapter<HabitEvent, HabitEventItemAdapter.HabitEventViewHolder> {
    // Initialize variables
    private static String DATE_FORMAT = "dd/MM/yyyy";
    private Context _context;
    private static Habit _parentHabit;
    private static String _userId;
    private static HabitEventList _habitEvents;
    private static HabitEventListFragment _habitEventListFragment;
    private Bitmap _imageReference;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options the firestore entities
     */
    public HabitEventItemAdapter(@NonNull FirestoreRecyclerOptions<HabitEvent> options,
                                 Habit parentHabit,
                                 String userId,
                                 HabitEventList habitEvents,
                                 HabitEventListFragment fragment) {
        super(options);
        _parentHabit = parentHabit;
        _userId = userId;
        _habitEvents = habitEvents;
        _habitEventListFragment = fragment;
    }

    @NonNull
    @Override
    public HabitEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate view based on HabitEventViewHolder
        _context = parent.getContext();
        View view = LayoutInflater.from(_context).inflate(R.layout.habit_event_row, parent, false);
        return new HabitEventViewHolder(view, _snapshots, _userId, _parentHabit, _context);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitEventViewHolder holder, int position, @NonNull HabitEvent habitEvent) {
        // Fetch image
        fetchEventImageFromDB(holder, habitEvent);

        // Set UI elements of habit event
        holder._habitEventDate.setText(new SimpleDateFormat(DATE_FORMAT).format(habitEvent.getDate()));
        holder._habitEventComment.setText(habitEvent.getComment());
        // TODO: Implement image setting too
    }

    private void fetchEventImageFromDB(@NonNull HabitEventViewHolder holder, HabitEvent event) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        StorageReference ref = storageReference.child("images/" + _userId + "/" + _parentHabit.getId() + "/" + event.getId() + "/eventImage");

        final long ONE_MEGABYTE = 1024 * 1024;

        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                _imageReference = bm;
                holder._habitEventImage.setImageBitmap(_imageReference);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed to get image");
            }
        });
    }

    /**
     * This class holds any necessary elements of the habit event on the front-end
     */
    public static class HabitEventViewHolder extends RecyclerView.ViewHolder {
        // List of UI elements
        private final TextView _habitEventDate;
        private final TextView _habitEventComment;
        private final ImageView _habitEventImage;

        /**
         * Default constructor
         * @param itemView (View): The habit event view
         */
        public HabitEventViewHolder(@NonNull View itemView,
                                    ObservableSnapshotArray<HabitEvent> _snapshots,
                                    String userId,
                                    Habit parentHabit,
                                    Context context) {
            super(itemView);

            // Connect UI elements
            _habitEventDate = itemView.findViewById(R.id.habit_event_date);
            _habitEventComment = itemView.findViewById(R.id.habit_event_comment);
            _habitEventImage = itemView.findViewById(R.id.habit_event_image);
        }
    }

}
