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
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class HabitListFragment extends Fragment {

    private static final String TAG = "HabitListFragment";

    // dont need this, will delete if ok
    private static final HabitList _habitList = new HabitList();

    private HabitItemAdapter _habitItemAdapter;
    private HabitListFragment _fragment = this;
    FirebaseFirestore _db = FirebaseFirestore.getInstance();
    final CollectionReference _collectionReference = _db.collection("Habits");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Context context = getContext();

        // populate the list with existing items in the database
        FirestoreRecyclerOptions<Habit> options = new FirestoreRecyclerOptions.Builder<Habit>()
                .setQuery(_db.collection("Habits"), Habit.class)
                .build();

        /*
         this extracts the habit IDs of the existing habits and puts it into a set of existing
         habit IDs to ensure only unique habit IDs are produced when adding new habits
         */
        Task<QuerySnapshot> querySnapshotTask = _db.collection("Habits").get();
        while (!querySnapshotTask.isComplete());
        List<DocumentSnapshot> snapshotList = querySnapshotTask.getResult().getDocuments();
        for (int i = 0; i < snapshotList.size(); i++) {
            Map<String, Object> extractMap = snapshotList.get(i).getData();
            Long id = (Long) extractMap.get("habitId");
            HabitItemAdapter._habitIdSet.add(id);
        }

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

            /*
            cast into HabitViewHolder so we can use setButtonVisible and
            setButtonsInvisible methods
             */
            HabitItemAdapter.HabitViewHolder habitViewHolder = (HabitItemAdapter.HabitViewHolder)
                    viewHolder;

            if (direction == ItemTouchHelper.LEFT) {
                habitViewHolder.setButtonsVisible();
            } else if (direction == ItemTouchHelper.RIGHT) {
                habitViewHolder.setButtonsInvisible();
            }
            _habitItemAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
        }
    };

    // deprecated function, will delete if ok
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
        HashMap<String, Object> habitData = new HashMap<>();

        //generate the habit ID for the added Habit
        Long habitId = HabitItemAdapter.generateHabitId();

        // initialize fields
        habitData.put("title", title);
        habitData.put("reason", reason);
        habitData.put("date", date);
        habitData.put("habitId", habitId);

        // add habit to database, using it's habit ID as the document name
        _collectionReference
                .document(habitId.toString())
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


    public void updateAfterEdit(String title, String reason, Date date, int pos,
                                HabitItemAdapter.HabitViewHolder viewHolder) {

        // this acquires the unique habit ID of the habit to be edited
        Long habitId = _habitItemAdapter._snapshots.get(pos).getHabitId();

        // stores the new fields of the Habit into a hashmap
        HashMap<String, Object> habitData = new HashMap<>();
        habitData.put("title", title);
        habitData.put("reason", reason);
        habitData.put("date", date);
        habitData.put("habitId", habitId);

        // replaces the old fields of the Habit with the new fields
        _collectionReference.document(habitId.toString())
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

        // will need to fix
        viewHolder.setButtonsInvisible();
        _habitItemAdapter.notifyDataSetChanged();
    }
}