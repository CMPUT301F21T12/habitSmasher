package com.example.habitsmasher.ui.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitEventList;
import com.example.habitsmasher.HabitList;
import com.example.habitsmasher.ListFragment;
import com.example.habitsmasher.R;
import com.example.habitsmasher.User;
import com.example.habitsmasher.UserDatabaseHelper;
import com.example.habitsmasher.listeners.SwipeListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;
import java.util.Map;

/**
 * UI class that represents and specifies the behaviour of the user interface
 * displayed when a user is accessing their own habit list
 *
 * @author Kaden Dreger, Jason Kim, Rudy Patel
 */
public class HabitListFragment extends ListFragment<Habit> {
    private static final String TAG = "HabitListFragment";
    private static final String CANCELLED_MESSAGE = "Cancelled swap";
    private static final String SWAP_MESSAGE = "Please select another Habit to swap with";

    // user who owns this list of habits displayed
    private User _user;

    private Context _context;

    // list of habits being displayed
    private HabitList _habitList;

    // adapter that connects the RecyclerView to the database
    // note: can extract this to list fragment once adapter interface is done
    private HabitItemAdapter _habitItemAdapter;
    private int _longPressedPosition;
    private boolean _longPressed;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        _context = getContext();

        _user = UserDatabaseHelper.getCurrentUser(_context);
        _habitList = _user.getHabits();

        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Habit List");


        // query firebase for all habits that correspond to the current user
        Query query = getListFromFirebase();

        // populate the list with existing items in the database
        FirestoreRecyclerOptions<Habit> options = new FirestoreRecyclerOptions.Builder<Habit>()
                .setQuery(query, Habit.class)
                .build();

        populateList(query);
        _habitItemAdapter = new HabitItemAdapter(options, _habitList, _user.getId(), true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_context,
                                                                    LinearLayoutManager.VERTICAL,
                                                                    false);

        View view = inflater.inflate(R.layout.fragment_habit_list, container, false);
        FloatingActionButton addHabitFab = view.findViewById(R.id.add_habit_fab);

        addHabitFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddDialogBox();
            }
        });

        initializeRecyclerView(layoutManager, view);
        return view;
    }

    /**
     * This helper method initializes the RecyclerView
     * @param layoutManager the associated LinearLayoutManager
     * @param view the associated View
     */
    public void initializeRecyclerView(LinearLayoutManager layoutManager, View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_items);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(_habitItemAdapter);

        /* Implementation of swipe menu functionality came from this source:
        Name: Velmurugan
        Date: March 4, 2021
        URL: https://howtodoandroid.com/android-recyclerview-swipe-menu
         */
        // create a touch listener which handles the click and swipe function of the RecyclerView
        RecyclerTouchListener touchListener = new RecyclerTouchListener(getActivity(), recyclerView);
        touchListener.setClickable(new RecyclerTouchListener.OnRowClickListener() {
            @Override
            // if row at the specified position is clicked
            public void onRowClicked(int position) {
                // check if a row has been long pressed
                if (_longPressed) {
                    // store the to and from positions
                    int fromPosition = _longPressedPosition;
                    int toPosition = position;

                    // handle case where you select the same habit to swap with
                    if(fromPosition == toPosition) {
                        Toast.makeText(_context, CANCELLED_MESSAGE, Toast.LENGTH_SHORT).show();
                        recyclerView.findViewHolderForAdapterPosition(_longPressedPosition).itemView.findViewById(R.id.habit_view_constraint).setBackgroundColor(Color.WHITE);
                        _longPressedPosition = -1;
                        _longPressed = false;
                        return;
                    }

                    // get the correspondong habits
                    Habit fromHabit = _habitItemAdapter.getItem(fromPosition);
                    Habit toHabit = _habitItemAdapter.getItem(toPosition);

                    // change habit item background color back to white
                    recyclerView.findViewHolderForAdapterPosition(_longPressedPosition).itemView.findViewById(R.id.habit_view_constraint).setBackgroundColor(Color.WHITE);

                    // swap the two habits
                    swapSortIndex(fromHabit, toHabit, fromPosition, toPosition);
                    _longPressed = false;
                    _longPressedPosition = -1;
                } else {
                    openViewWindowForItem(position);
                }
            }
        })
                .setSwipeOptionViews(R.id.edit_button, R.id.delete_button)
                .setSwipeable(R.id.habit_view, R.id.swipe_options,
                        new SwipeListener(this));

        touchListener.setLongClickable(false, new RecyclerTouchListener.OnRowLongClickListener() {

            // this checks for long presses
            @Override
            public void onRowLongClicked(int longPosition) {
                // handle case where you long press on same item twice
                if(longPosition == _longPressedPosition) {
                    Toast.makeText(_context, CANCELLED_MESSAGE, Toast.LENGTH_SHORT).show();
                    recyclerView.findViewHolderForAdapterPosition(_longPressedPosition).itemView.findViewById(R.id.habit_view_constraint).setBackgroundColor(Color.WHITE);
                    _longPressedPosition = -1;
                    _longPressed = false;
                    return;
                }
                // save position of long pressed habit item
                _longPressedPosition = longPosition;
                _longPressed = true;
                // change background color of long pressed item
                recyclerView.findViewHolderForAdapterPosition(longPosition).itemView.findViewById(R.id.habit_view_constraint).setBackgroundColor(getResources().getColor(R.color.light_grey));
                // send toast message to remind user of how to swap habits
                Toast.makeText(_context, SWAP_MESSAGE, Toast.LENGTH_LONG).show();
            }
        });
        // connect listener to recycler view
        recyclerView.addOnItemTouchListener(touchListener);
    }

    /**
     * This function swaps the position of two given habits in the list
     * @param fromHabit The habit being swapped (long pressed)
     * @param toHabit The habit to be swapped (clicked)
     * @param fromPosition The position of fromHabit
     * @param toPosition The position of toHabit
     */
    private void swapSortIndex(Habit fromHabit, Habit toHabit, int fromPosition, int toPosition) {
        // Set sort indexes locally
        long tempIndex = _habitItemAdapter._snapshots.get(fromPosition).getSortIndex();
        fromHabit.setSortIndex(_habitItemAdapter._snapshots.get(toPosition).getSortIndex());
        toHabit.setSortIndex(tempIndex);

        // Set indexes in db
        _habitList.editHabitInDatabase(fromHabit, _user.getId());
        _habitList.editHabitInDatabase(toHabit, _user.getId());
    }

    /**
     * This method queries the database for all habits that correspond to the specified user
     * @return resulting firebase query
     */
    @NonNull
    public Query getListFromFirebase() {
        return _db.collection("Users")
                  .document(_user.getId())
                  .collection("Habits")
                    .orderBy("sortIndex",Query.Direction.ASCENDING);
    }

    protected void populateList(Query query) {
        //get all of the habits
        Task<QuerySnapshot> querySnapshotTask = query.get();

        /*
        populate HabitList with current Habits and habit IDs to initialize state to match
        database, fills when habitList is empty and snapshot is not, which is only
        when app is initially launched
        */
        if (_habitList.getHabitList().isEmpty()) {
            // wait for all the snapshots to come in
            while (!querySnapshotTask.isComplete()) ;

            // make a list of all of the habit snapshots
            List<DocumentSnapshot> snapshotList = querySnapshotTask.getResult().getDocuments();

            // convert all of the snapshots into proper habits
            for (int i = 0; i < snapshotList.size(); i++) {
                // get the data and convert to hashmap, also print to log
                Map<String, Object> extractMap = snapshotList.get(i).getData();
                Log.d(TAG, extractMap.toString());

                // get all of the data from the snapshot
                String title = (String) extractMap.get("title");
                String reason = (String) extractMap.get("reason");
                Timestamp date = (Timestamp) extractMap.get("date");
                String id = (String) extractMap.get("id");
                String days = extractMap.get("days").toString();
                boolean isPublic = (boolean) extractMap.get("public");
                Long sortIndex = (long) extractMap.get("sortIndex");

                // create a new habit with the snapshot data
                Habit addHabit = new Habit(title, reason, date.toDate(), days, isPublic, id, new HabitEventList(), sortIndex);

                // add the habit to the local list
                _habitList.addHabitLocal(addHabit);
            }
        }
    }

    /**
     * This function gets the largest sort index in the list
     * @return Returns the largest sort index
     */
    public Long getLargestSortIndex() {
        long maxSortIndex = 0;
        // This iterates through the habits to update their sort indexes
        for (Habit habit : _habitItemAdapter._snapshots){
            if (habit.getSortIndex() > maxSortIndex) {
                maxSortIndex = habit.getSortIndex();
            }
        }
        return maxSortIndex;
    }

    // we can extract these two methods to list fragment once item adapter interface is done!
    @Override
    public void onStart() {
        super.onStart();
        _habitItemAdapter.startListening();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        _habitItemAdapter.stopListening();
    }

    /**
     * This helper method is responsible for opening the add habit dialog box
     */
    protected void openAddDialogBox() {
        AddHabitDialog addHabitDialog = new AddHabitDialog();
        addHabitDialog.setCancelable(true);
        addHabitDialog.setTargetFragment(this, 1);
        addHabitDialog.show(getFragmentManager(), "AddHabitDialog");
    }


    public void openEditDialogBox(int position) {
        EditHabitDialog editHabitFragment = new EditHabitDialog(position,
                _habitItemAdapter._snapshots.get(position));
        editHabitFragment.setCancelable(true);
        editHabitFragment.setTargetFragment(this, 1);
        editHabitFragment.show(getFragmentManager(),
                "Edit Habit");
    }

    // note: add this to list fragment class once view is implemented for habitevents
    protected void openViewWindowForItem(int position) {
        // Get the selected habit
        Habit currentHabit = _habitItemAdapter._snapshots.get(position);

        // Create a bundle to be passed into the habitViewFragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("habit", currentHabit);
        bundle.putSerializable("userId", _user.getId());
        bundle.putSerializable("isOwner", true);
        NavController controller = NavHostFragment.findNavController(this);

        // Navigate to the habitViewFragment
        controller.navigate(R.id.action_navigation_dashboard_to_habitViewFragment, bundle);
    }

    /**
     * Updates the displayed habit list after an add operation
     * @param addedHabit
     */
    public void updateListAfterAdd(Habit addedHabit){
        _habitList.addHabitToDatabase(addedHabit, _user.getId());
    }

    /**
     * Updates the displayed habit list after an edit operation
     * @param editedHabit
     * @param pos
     */
    public void updateListAfterEdit(Habit editedHabit, int pos) {
        _habitList.editHabitInDatabase(editedHabit, _user.getId());
        _habitItemAdapter.notifyItemChanged(pos);
    }

    /**
     * Updates the local habit list after the deletion operation
     * @param pos position of object in list
     */
    public void updateListAfterDelete(int pos) {
        Habit habitToDelete = _habitItemAdapter._snapshots.get(pos);

        deleteHabitEvents(_user.getId(), habitToDelete);

        _habitList.deleteHabit(getActivity(),
                _user.getId(),
                habitToDelete,
                pos);

        // This iterates through the habits to update their sort indexes
        for (Habit habit : _habitItemAdapter._snapshots){
            if (habit.getSortIndex() > habitToDelete.getSortIndex()) {
                habit.setSortIndex(habit.getSortIndex() - 1);
                _habitList.editHabitInDatabase(habit, _user.getId());
            }
        }
    }

    /**
     * Deletes all child habit events of a habit
     * @param userId (String) The current user's username
     * @param parentHabit (Habit) The habit to delete
     */
    private void deleteHabitEvents(String userId, Habit parentHabit) {
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
        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Deleted habit events");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed to delete habit events");
            }
        });
    }
}