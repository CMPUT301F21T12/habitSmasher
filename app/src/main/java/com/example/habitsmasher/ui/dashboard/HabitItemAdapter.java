package com.example.habitsmasher.ui.dashboard;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitEvent;
import com.example.habitsmasher.HabitEventList;
import com.example.habitsmasher.HabitList;
import com.example.habitsmasher.ItemAdapter;
import com.example.habitsmasher.ProgressTracker;
import com.example.habitsmasher.R;
import com.example.habitsmasher.listeners.FailureListener;
import com.example.habitsmasher.listeners.SuccessListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;
import java.util.Map;


/**
 * Custom adapter class that is used to connect the Firestore database and the
 * RecyclerView displaying the habits
 *
 * @author Jason Kim, Jacob Nguyen, Rudy Pate, Cameron Matthew
 */
public class HabitItemAdapter extends ItemAdapter<Habit, HabitItemAdapter.HabitViewHolder> {
    private static HabitList _habits;
    private final String _userId;
    private Context _context;
    private boolean _swipable;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     * @param options the firestore entities
     * @param habits the habit list
     * @param userId the username of the current user
     * @param swipable Whether the row is swipable or not
     */
    public HabitItemAdapter(@NonNull FirestoreRecyclerOptions<Habit> options,
                            HabitList habits,
                            String userId,
                            boolean swipable) {
        super(options);
        _habits = habits;
        _userId = userId;
        _swipable = swipable;
    }



    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        _context = parent.getContext();
        View view;
        if (_swipable) {
            view = LayoutInflater.from(_context).inflate(R.layout.habit_row, parent, false);
        }
        else {
            view = LayoutInflater.from(_context).inflate(R.layout.habit_row_not_swipable, parent, false);
        }
        return new HabitViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull HabitViewHolder holder,
                                    int position,
                                    @NonNull Habit habit) {
        // set necessary elements of the habit
        holder._habitTitle.setText(habit.getTitle());

        // Fetch habit events for a habit from the database
        habit.setHabitEvents(new HabitEventList());
        populateEventList(getEventListFromFirebase(habit), habit);

        // Create a new progress tracker
        ProgressTracker progressTracker = new ProgressTracker(habit);

        // Get integer representation of progress
        int progress = (int) progressTracker.calculateProgressPercentage();

        // Set progress in circular progress and text
        holder._progressBar.setProgress(progress);
        String progressText = Integer.toString(progress) + "%";
        holder._progressText.setText(progressText);
    }

    /**
     * Creates a query to get the list of habit events from the database
     * @param parentHabit (Habit) The habit for which events are being queried
     * @return The query
     */
    @NonNull
    protected Query getEventListFromFirebase(Habit parentHabit) {
        FirebaseFirestore db =  FirebaseFirestore.getInstance();

        // Query is made of username, habit name, and events
        Query query = db.collection("Users")
                .document(_userId)
                .collection("Habits")
                .document(parentHabit.getId())
                .collection("Events");
        return query;
    }

    /**
     * Populate the list of habit events for a specific habit
     * @param query (Query) The query parameters
     * @param parentHabit (Habit) The habit for which events are being queried
     */
    protected void populateEventList(Query query, Habit parentHabit) {
        Task<QuerySnapshot> querySnapshotTask = query.get();

            /*
            populate HabitList with current Habits and habit IDs to initialize state to match
            database, fills when habitList is empty and snapshot is not, which is only
            when app is initially launched
            */
        if (parentHabit.getHabitEvents().isEmpty()) {
            // wait for snapshots to come in
            while (!querySnapshotTask.isComplete());

            //make a list of all the habit event snapshots
            List<DocumentSnapshot> snapshotList = querySnapshotTask.getResult().getDocuments();

            for (int i = 0; i < snapshotList.size(); i++) {
                // extract the data from the snapshot
                Map<String, Object> extractMap = snapshotList.get(i).getData();
                String comment = (String) extractMap.get("comment");
                Timestamp date = (Timestamp) extractMap.get("date");
                String location = (String) extractMap.get("location");
                String id = extractMap.get("id").toString();

                // create the new habit event from the snapshot data and add to local list
                HabitEvent addHabitEvent = new HabitEvent(date.toDate(),
                        comment,
                        id,
                        location
                );
                parentHabit.getHabitEvents().addHabitEventLocally(addHabitEvent);
            }
        }
    }

    /**
     * Class that holds the necessary elements of an individual row in the HabitList
     */
    public static class HabitViewHolder extends RecyclerView.ViewHolder {
        private final TextView _habitTitle;
        private final ConstraintLayout _habitRows;
        private final ProgressBar _progressBar;
        private final TextView _progressText;

        /**
         * Constructs a view holder
         * @param itemView view of row of RecyclerView which is held in ViewHolder
         */
        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            _habitRows = itemView.findViewById(R.id.habit_rows);
            _habitTitle = itemView.findViewById(R.id.habit_title);
            _progressBar = itemView.findViewById(R.id.habit_progress_bar);
            _progressText = itemView.findViewById(R.id.habit_progress_textview);
        }

        /**
         * Deletes all child habit events of a habit
         * @param userId (String) The current user's username
         * @param parentHabit (Habit) The habit to delete
         */
        public void deleteHabitEvents(String userId, Habit parentHabit) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // get all of the habit events
            Task<QuerySnapshot> querySnapshotTask = db.collection("Users")
                    .document(userId)
                    .collection("Habits")
                    .document(parentHabit.getId())
                    .collection("Events")
                    .get();

            // waiting for all the documents
            while (!querySnapshotTask.isComplete());

            // make a list of all the documents
            List<DocumentSnapshot> snapshotList = querySnapshotTask.getResult().getDocuments();

            // delete all the events
            WriteBatch batch = db.batch();
            for (int i = 0; i < snapshotList.size(); i++) {
                batch.delete(snapshotList.get(i).getReference());
            }
            batch.commit().addOnSuccessListener(new SuccessListener(TAG, "Deleted habit events"))
            .addOnFailureListener(new FailureListener(TAG, "Failed to delete habit events"));
        }
    }
}

