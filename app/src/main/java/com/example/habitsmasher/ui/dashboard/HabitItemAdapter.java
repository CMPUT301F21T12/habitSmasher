package com.example.habitsmasher.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitList;
import com.example.habitsmasher.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;

import java.util.Locale;

/**
 * The habit item adapter is the custom adapter for the RecyclerView that holds the habit list
 */
public class HabitItemAdapter extends FirestoreRecyclerAdapter<Habit, HabitItemAdapter.HabitViewHolder> {

    private static final String DATE_PATTERN = "dd-MM-yyyy";
    private static final Locale LOCALE = Locale.CANADA;
    private Context _context;
    private static HabitList _habits;
    private static HabitListFragment _habitListFragment;
    private final FragmentActivity _activity;
    private final ObservableSnapshotArray<Habit> _snapshots;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options the firestore entities
     */
    public HabitItemAdapter(@NonNull FirestoreRecyclerOptions<Habit> options, FragmentActivity activity,HabitList habits,HabitListFragment fragment) {
        super(options);
        _snapshots = options.getSnapshots();
        _activity = activity;
        _habits = habits;
        _habitListFragment = fragment;
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


        setOnClickListenerForHabit(holder, position);
    }

    /*
    @Override
    public void onDataChanged() {
        // populate local List to match database
        if (_habits.getHabitList().isEmpty()) {
            populateHabitList();
        }
        super.onDataChanged();
    }
    */

    private void setOnClickListenerForHabit(@NonNull HabitViewHolder holder, int position) {
        holder._habitRows.setOnClickListener(view -> openHabitView(holder, position));
    }

    /**
     * This function opens the habit view
     * @param holder
     * This holds the values for the selected habit item
     * @param position
     * This is the position of the selected habit item
     */
    private void openHabitView(HabitViewHolder holder, int position) {
        Habit currentHabit = _habits.getHabitList().get(position);
        // Create Habit View Fragment with all required parameters passed in
        HabitViewFragment fragment = HabitViewFragment.newInstance(currentHabit.getReason(), currentHabit.getDate().toString());
        // Replace the current fragment with the habit view
        FragmentTransaction transaction = _activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, fragment);
        transaction.addToBackStack(null);
        // Load new fragment
        transaction.commit();
    }

    @Override
    public int getItemCount() {
        return _habits.getHabitList().size();
    }

    /*
    public void populateHabitList() {
        for (int i = 0; i < _snapshots.size(); i++) {
            Habit addHabit = _snapshots.get(i);
            _habits.getHabitList().add(addHabit);
        }
    }
     */

    public static class HabitViewHolder extends RecyclerView.ViewHolder {
        private final TextView _habitTitle;
        private final ConstraintLayout _habitRows;
        private Button _editButton;
        private Button _deleteButton;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);

            _habitRows = itemView.findViewById(R.id.habit_rows);
            _habitTitle = itemView.findViewById(R.id.habit_title);
            _editButton = itemView.findViewById(R.id.edit_button);
            _deleteButton = itemView.findViewById(R.id.delete_button);

            _editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int buttonPos = getAdapterPosition();
                    EditHabitFragment editHabitFragment = new EditHabitFragment(buttonPos,
                            _habits.getHabitList().get(buttonPos), _habitListFragment);
                    editHabitFragment.show(_habitListFragment.getFragmentManager(), "Edit Habit");
                }
            });

            _deleteButton.setOnClickListener(new View.OnClickListener() {
                int buttonPos = getAdapterPosition();
                @Override
                public void onClick(View v) {
                    /*
                    Delete logic goes here
                     */
                }
            });

        }
    }
}

