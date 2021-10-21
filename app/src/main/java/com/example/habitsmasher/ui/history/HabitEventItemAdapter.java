package com.example.habitsmasher.ui.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitsmasher.HabitEvent;
import com.example.habitsmasher.HabitEventList;
import com.example.habitsmasher.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;

public class HabitEventItemAdapter extends FirestoreRecyclerAdapter<HabitEvent, HabitEventItemAdapter.HabitEventViewHolder> {
    private Context _context;
    private final FragmentActivity _activity;
    private final ObservableSnapshotArray<HabitEvent> _snapshots;
    private static HabitEventList _habitEvents;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options the firestore entities
     */
    public HabitEventItemAdapter(@NonNull FirestoreRecyclerOptions<HabitEvent> options, FragmentActivity activity) {
        super(options);
        _snapshots = options.getSnapshots();
        _activity = activity;
    }

    @NonNull
    @Override
    public HabitEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        _context = parent.getContext();
        View view = LayoutInflater.from(_context).inflate(R.layout.habit_event_row, parent, false);
        return new HabitEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitEventViewHolder holder, int position, @NonNull HabitEvent habitEvent) {
        // Set UI elements of habit event
        holder._habitEventDate.setText(habitEvent.getStartDate().toString());
        holder._habitEventComment.setText(habitEvent.getComment());

        // TODO: Implement image setting too
        // TODO: Set on click listener
    }

    @Override
    public int getItemCount() { return _habitEvents.getHabitEvents().size(); }

    /**
     * This class holds any necessary elements of the habit event on the front-end
     */
    public static class HabitEventViewHolder extends RecyclerView.ViewHolder {
        // List of UI elements
        private final TextView _habitEventDate;
        private final TextView _habitEventComment;
        // private final ImageView _habitEventImage;
        // private final ConstraintLayout _habitEventRow;

        /**
         * Default constructor
         * @param itemView (View): The habit event view
         */
        public HabitEventViewHolder(@NonNull View itemView) {
            super(itemView);

            _habitEventDate = itemView.findViewById(R.id.habit_event_date);
            _habitEventComment = itemView.findViewById(R.id.habit_event_comment);
            // _habitEventImage = itemView.findViewById(R.id.habit_event_image);
            // _habitEventRow = itemView.findViewById(R.id.habit_event_row);
        }
    }

}
