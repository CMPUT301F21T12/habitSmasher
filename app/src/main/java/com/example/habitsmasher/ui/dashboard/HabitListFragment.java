package com.example.habitsmasher.ui.dashboard;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 */
public class HabitListFragment extends ListFragment<Habit> {

    private static final String TAG = "HabitListFragment";
    private static final String USER_DATA_PREFERENCES_TAG = "USER_DATA";

    // user who owns this list of habits displayed
    private User _user;

    private Context _context;

    // list of habits being displayed
    private HabitList _habitList;

    // adapter that connects the RecyclerView to the database
    // note: can extract this to list fragment once adapter interface is done
    private HabitItemAdapter _habitItemAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        _context = getContext();

        _user = getCurrentUser();
        _habitList = _user.getHabits();

        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Habit List");


        // query firebase for all habits that correspond to the current user
        Query query = getListFromFirebase();

        // populate the list with existing items in the database
        FirestoreRecyclerOptions<Habit> options = new FirestoreRecyclerOptions.Builder<Habit>()
                .setQuery(query, Habit.class)
                .build();

        populateList(query);
        _habitItemAdapter = new HabitItemAdapter(options, _habitList, _user.getUsername());
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
                openViewWindowForItem(position);
            }
        })
                .setSwipeOptionViews(R.id.edit_button, R.id.delete_button)
                .setSwipeable(R.id.habit_view, R.id.swipe_options, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        // edit and delete functionality below
                        switch (viewID){
                            // if edit button clicked
                            case R.id.edit_button:
                                openEditDialogBox(position);
                                break;
                            // if delete button clicked
                            case R.id.delete_button:
                                updateListAfterDelete(position);
                                break;
                        }

                    }
                });
        // connect listener to recycler view
        recyclerView.addOnItemTouchListener(touchListener);
    }

    /**
     * This method queries the database for all habits that correspond to the specified user
     * @return resulting firebase query
     */
    @NonNull
    public Query getListFromFirebase() {
        return _db.collection("Users")
                  .document(_user.getId())
                  .collection("Habits");
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
                String days = (String) extractMap.get("days");

                // create a new habit with the snapshot data
                Habit addHabit = new Habit(title, reason, date.toDate(), days, id, new HabitEventList());

                // add the habit to the local list
                _habitList.addHabitLocal(addHabit);
            }
        }
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


    // note: add this to list fragment class once swipe is complete in habit event list
    protected void openEditDialogBox(int position) {
        EditHabitFragment editHabitFragment = new EditHabitFragment(position,
                _habitItemAdapter._snapshots.get(position),
                this);
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
        _habitList.editHabitInDatabase(editedHabit, pos, _user.getId());
        _habitItemAdapter.notifyItemChanged(pos);
    }

    // add to list fragment class once swipe is fixed in habit events
    public void updateListAfterDelete(int position) {
        Habit habitToDelete = _habitItemAdapter._snapshots.get(position);

        deleteHabitEvents(_user.getId(), habitToDelete);

        _habitList.deleteHabit(getActivity(),
                _user.getId(),
                habitToDelete,
                position);
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

    @NonNull
    private User getCurrentUser() {
        SharedPreferences sharedPref = _context.getSharedPreferences(USER_DATA_PREFERENCES_TAG, Context.MODE_PRIVATE);

        String username = sharedPref.getString("username", "user");
        String userId = sharedPref.getString("userId", "id");
        String email = sharedPref.getString("email", "email");
        String password = sharedPref.getString("password", "password");

        return new User(userId, username, email, password);
    }


}