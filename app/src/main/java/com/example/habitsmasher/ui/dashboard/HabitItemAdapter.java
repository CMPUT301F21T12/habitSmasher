package com.example.habitsmasher.ui.dashboard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitList;
import com.example.habitsmasher.R;
import com.example.habitsmasher.listeners.FailureListener;
import com.example.habitsmasher.listeners.SuccessListener;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;


/**
 * Custom adapter class that is used to connect the Firestore database and the
 * RecyclerView displaying the habits
 */
public class HabitItemAdapter extends FirestoreRecyclerAdapter<Habit, HabitItemAdapter.HabitViewHolder> {
    private static HabitList _habits;
    private final String _userId;
    public final ObservableSnapshotArray<Habit> _snapshots;
    private Context _context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     * @param options the firestore entities
     * @param habits the habit list
     * @param fragment the habitListFragment
     * @param userId the username of the current user
     */
    public HabitItemAdapter(@NonNull FirestoreRecyclerOptions<Habit> options,
                            HabitList habits,
                            String userId) {
        super(options);
        _snapshots = options.getSnapshots();
        _habits = habits;
        _userId = userId;
    }



    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        _context = parent.getContext();
        View view = LayoutInflater.from(_context).inflate(R.layout.habit_row, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull HabitViewHolder holder,
                                    int position,
                                    @NonNull Habit habit) {
        // set necessary elements of the habit
        holder._habitTitle.setText(habit.getTitle());
    }

    @Override
    public int getItemCount() {
        return _snapshots.size();
    }


    /**
     * Class that holds the necessary elements of an individual row in the HabitList
     */
    public static class HabitViewHolder extends RecyclerView.ViewHolder {
        private final TextView _habitTitle;
        private final ConstraintLayout _habitRows;

        /**
         * Constructs a view holder
         * @param itemView view of row of RecyclerView which is held in ViewHolder
         */
        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            _habitRows = itemView.findViewById(R.id.habit_rows);
            _habitTitle = itemView.findViewById(R.id.habit_title);
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

