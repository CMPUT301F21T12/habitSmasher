package com.example.habitsmasher.ui.history;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.example.habitsmasher.HabitEvent;
import com.example.habitsmasher.HabitEventList;
import com.example.habitsmasher.ListFragment;
import com.example.habitsmasher.ProgressTracker;
import com.example.habitsmasher.R;
import com.example.habitsmasher.listeners.FailureListener;
import com.example.habitsmasher.listeners.SuccessListener;
import com.example.habitsmasher.ui.dashboard.RecyclerTouchListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * HabitEventListFragment class
 * Responsible for data and UI handling of the habit event list
 */
public class HabitEventListFragment extends ListFragment<HabitEvent> {
    // Initialize variables
    private static final String TAG = "HabitEventListFragment";
    private static final String USER_DATA_PREFERENCES_TAG = "USER_DATA";
    private static final String USER_ID_SHARED_PREF_TAG = "userId";

    // can extract this to list fragment once adapter interface is done
    private HabitEventItemAdapter _habitEventItemAdapter;
    private Habit _parentHabit;
    private String _userId;
    private HabitEventList _habitEventList;


    public HabitEventListFragment () {
        // Required empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            _parentHabit = (Habit) getArguments().getSerializable("parentHabit");
        }

        SharedPreferences sharedPref = getContext().getSharedPreferences(USER_DATA_PREFERENCES_TAG, Context.MODE_PRIVATE);
        _userId = sharedPref.getString(USER_ID_SHARED_PREF_TAG, "user");

        _parentHabit.setHabitEvents(new HabitEventList());
        _habitEventList = _parentHabit.getHabitEvents();

        // Get context
        Context context = getContext();

        try {
            // Get query
            Query query = getListFromFirebase();

            // Populate the list with existing items in the database
            FirestoreRecyclerOptions<HabitEvent> options = new FirestoreRecyclerOptions.Builder<HabitEvent>()
                    .setQuery(query, HabitEvent.class)
                    .build();
            populateList(query);
            // Set item adapter and habit event list
            _habitEventItemAdapter = new HabitEventItemAdapter(options,
                                                               _parentHabit,
                                                               _userId,
                                                               _habitEventList,
                                                                       this);
        }
        catch (NullPointerException e){
            // Try catch statement is needed so code doesn't break if there's no events yet, and thus no possible query
            Log.d(TAG, "No habit events to show");
        }

        // Inflate habit event list view
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        View view = inflater.inflate(R.layout.fragment_habit_event_list, container, false);

        // Set header title
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Habit Event List");

        // Connect new habit fab button, add listener which opens new event dialog
        FloatingActionButton addHabitEventFab = view.findViewById(R.id.add_habit_event_fab);
        addHabitEventFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddDialogBox();
            }
        });

        // Initialize recycler view and return view
        initializeRecyclerView(layoutManager, view);

        return view;
    }

    /**
     * Initialize the recycler view
     * @param layoutManager Manager of the layout of the recycler view to initialize
     * @param view The layout of the recycler view to initialize
     */
    protected void initializeRecyclerView(LinearLayoutManager layoutManager, View view) {
        // Find recycler view
        RecyclerView recyclerView = view.findViewById(R.id.habit_events_recycler_view);

        // Set needed elements
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(_habitEventItemAdapter);

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
                .setSwipeOptionViews(R.id.edit_habit_event_button, R.id.delete_habit_event_button)
                .setSwipeable(R.id.habit_event_view, R.id.edit_habit_event_swipe_options, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        // edit and delete functionality below
                        switch (viewID){
                            // if edit button clicked
                            case R.id.edit_habit_event_button:
                                openEditDialogBox(position);
                                break;
                            // if delete button clicked
                            case R.id.delete_habit_event_button:
                                updateListAfterDelete(position);
                                break;
                        }

                    }
                });
        recyclerView.addOnItemTouchListener(touchListener);
    }

    /**
     * Gets the proper query string
     * @return
     */
    @NonNull
    protected Query getListFromFirebase() {
        // Query is made of username, habit name, and events
        Query query = _db.collection("Users")
                        .document(_userId)
                        .collection("Habits")
                        .document(_parentHabit.getId())
                        .collection("Events");
        return query;
    }

    @Override
    protected void populateList(Query query) {
        Task<QuerySnapshot> querySnapshotTask = query.get();
            /*
            populate HabitList with current Habits and habit IDs to initialize state to match
            database, fills when habitList is empty and snapshot is not, which is only
            when app is initially launched
            */
        if (_habitEventList.getHabitEvents().isEmpty()) {
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
                HabitEvent addHabitEvent = new HabitEvent(date.toDate(), comment, id,
                                                          location);
                Log.d(TAG, addHabitEvent.getId());
                _habitEventList.addHabitEventLocally(addHabitEvent);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Enable live updates
        _habitEventItemAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        // Stop live updates
        _habitEventItemAdapter.stopListening();
    }

    @Override
    protected void openAddDialogBox() {
        // Create new AddHabitEventDialog and show it
        AddHabitEventDialog addHabitEventDialog = new AddHabitEventDialog();
        addHabitEventDialog.setTargetFragment(HabitEventListFragment.this, 1);
        addHabitEventDialog.show(getFragmentManager(), "AddHabitEventDialog");
    }

    public void openEditDialogBox(int position) {
        EditHabitEventDialog editHabitEventDialog = new EditHabitEventDialog(position, _habitEventItemAdapter._snapshots.get(position), _userId, _parentHabit);
        editHabitEventDialog.setCancelable(true);
        editHabitEventDialog.setTargetFragment(this, 1);
        editHabitEventDialog.show(getFragmentManager(), "Edit Habit Event");
    }

    /**
     * Add a new habit event to the database
     * @param addedHabitEvent
     */
    public void updateListAfterAdd(HabitEvent addedHabitEvent) {
        _habitEventList.addHabitEventToDatabase(addedHabitEvent,
                                                _userId,
                                                _parentHabit);
    }

    public void addHabitEvent(HabitEvent addedHabitEvent, Uri imageToAdd) {
        updateListAfterAdd(addedHabitEvent);
        _habitEventList.addImageToDatabase(_userId, _parentHabit, imageToAdd, addedHabitEvent.getId());
    }

    /**
     * Update a habit event after it has been edited
     * @param editedHabitEvent
     * @param pos (int) The position of the habit event in the list
     */
    @Override
    public void updateListAfterEdit(HabitEvent editedHabitEvent, int pos) {
        _habitEventItemAdapter.notifyItemChanged(pos);
    }

    public void editHabitEvent(HabitEvent editedHabitEvent,int pos, Uri habitImage) {
        _habitEventList.editHabitInDatabase(editedHabitEvent, _userId, _parentHabit, habitImage);
        updateListAfterEdit(editedHabitEvent, pos);
    }

    // TODO: add to list fragment class once swipe is fixed in habit events
    public void updateListAfterDelete(int position) {
        HabitEvent habitEventToDelete = _habitEventItemAdapter._snapshots.get(position);
        _habitEventList.deleteHabitEvent(getActivity(),
                _userId,
                _parentHabit,
                habitEventToDelete
        );
    }

    // TODO: add this to list fragment class once view is implemented for habitevents
    protected void openViewWindowForItem(int position) {
        // Get the selected habit
        HabitEvent currentHabitEvent = _habitEventItemAdapter._snapshots.get(position);

        // Create a bundle to be passed into the habitViewFragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("habitEvent", currentHabitEvent);
        bundle.putSerializable("parentHabit", _parentHabit);
        bundle.putSerializable("userId", _userId);
        NavController controller = NavHostFragment.findNavController(this);

        // Navigate to the habitViewFragment
        controller.navigate(R.id.action_navigation_habitEventList_to_habitEventView, bundle);
    }

    @Override
    public void onDestroyView() { super.onDestroyView();}
}
