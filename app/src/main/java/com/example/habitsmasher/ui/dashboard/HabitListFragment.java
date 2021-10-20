package com.example.habitsmasher.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitList;
import com.example.habitsmasher.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Date;
import java.util.HashMap;

public class HabitListFragment extends Fragment {
    private static final String TAG = "DashboardFragment";
    private final HabitList _habitList = new HabitList();
    private HabitItemAdapter _habitItemAdapter;
    private Button _editButton;
    private Button _deleteButton;
    private HabitListFragment _fragment = this;
    FirebaseFirestore _db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Context context = getContext();
        // populate the list with existing items in the database
        FirestoreRecyclerOptions<Habit> options = new FirestoreRecyclerOptions.Builder<Habit>()
                .setQuery(_db.collection("Habits"), Habit.class)
                .build();
        _habitItemAdapter = new HabitItemAdapter(options, getActivity(), _habitList, _fragment);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                                                                    LinearLayoutManager.VERTICAL,
                                                                    false);

        View view = inflater.inflate(R.layout.fragment_habit_list, container, false);

        FloatingActionButton addHabitFab = view.findViewById(R.id.add_habit_fab);
        /**
         * When fab is pressed, method call to open dialog fragment.
         */
        addHabitFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddHabitDialogBox();
            }
        });

        initializeRecyclerView(layoutManager, view);
        return view;
    }

    @Override public void onStart() {
        super.onStart();
        _habitItemAdapter.startListening();
    }

    @Override public void onStop()
    {
        super.onStop();
        _habitItemAdapter.stopListening();
    }

    /**
     * This helper method is responsible for opening the add habit dialog box
     */
    private void openAddHabitDialogBox() {
        AddHabitDialog addHabitDialog = new AddHabitDialog();
        addHabitDialog.setCancelable(true);
        addHabitDialog.setTargetFragment(HabitListFragment.this, 1);
        addHabitDialog.show(getFragmentManager(), "AddHabitDialog");
    }

    /**
     * This helper method initializes the RecyclerView
     * @param layoutManager the associated LinearLayoutManager
     * @param view the associated View
     */
    private void initializeRecyclerView(LinearLayoutManager layoutManager, View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_items);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(_habitItemAdapter);
        new ItemTouchHelper(_itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    /**
     * The implementation of the swipe to delete functionality below came from the following URL:
     * https://stackoverflow.com/questions/33985719/android-swipe-to-delete-recyclerview
     *
     *
     * Name: Rahul Raina
     * Date: November 2, 2016
     */
    //this probably won't be the way to do it, looks ugly as hell
    ItemTouchHelper.SimpleCallback _itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // if the habit row is swiped to the left, spawn edit and delete button
            // if swiped to the right, despawn them
            View habitView = viewHolder.itemView;
            _editButton = habitView.findViewById(R.id.edit_button);
            _deleteButton = habitView.findViewById(R.id.delete_button);

            if (direction == ItemTouchHelper.LEFT) {
                _editButton.setVisibility(View.VISIBLE);
                _deleteButton.setVisibility(View.VISIBLE);
            } else if (direction == ItemTouchHelper.RIGHT) {
                _editButton.setVisibility(View.INVISIBLE);
                _deleteButton.setVisibility(View.INVISIBLE);
            }
            _habitItemAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
        }
    };

    public void addNewHabit(Habit habit) {
        _habitList.addHabit(habit);
    }

    /**
     * This method is responsible for adding a new habit to the firestore database
     * @param title the habit title
     * @param reason the habit reason
     * @param date the habit date
     */
    public void addHabitToDatabase(String title, String reason, Date date){
        // Handling of adding a habit to firebase
        final CollectionReference collectionReference = _db.collection("Habits");
        HashMap<String, Object> habitData = new HashMap<>();

        habitData.put("title", title);
        habitData.put("reason", reason);
        habitData.put("date", date);

        collectionReference
                .document(title)
                .set(habitData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Data successfully added.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data failed to be added." + e.toString());
                    }
                });
    }

    @Override
    public void onDestroyView() { super.onDestroyView();
    }


    public void updateAfterEdit(String title, String reason, Date date, int pos) {
        final CollectionReference collectionReference = _db.collection("Habits");
        String oldHabitTitle = _habitList.getHabitList().get(pos).getTitle();
        _habitList.editHabit(title, reason, date, pos);
        // storing in database
        HashMap<String, Object> habitData = new HashMap<>();
        habitData.put("title", title);
        habitData.put("reason", reason);
        habitData.put("date", date);
        collectionReference.document(oldHabitTitle)
                .set(habitData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Data successfully added.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data failed to be added." + e.toString());
                    }
                });;
        _habitItemAdapter.notifyItemChanged(pos);
    }
}