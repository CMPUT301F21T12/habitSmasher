package com.example.habitsmasher.ui.history;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitEvent;
import com.example.habitsmasher.HabitEventList;
import com.example.habitsmasher.R;
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
 * Images for habit events are not implemented yet
 */
public class HabitEventListFragment extends Fragment {
    // Initialize variables
    private static final String TAG = "HabitEventListFragment";

    private HabitEventItemAdapter _habitEventItemAdapter;
    private Habit _parentHabit;
    private String _userId;
    private HabitEventList _habitEventList;
    private HabitEventListFragment _fragment = this;

    FirebaseFirestore _db = FirebaseFirestore.getInstance();

    public HabitEventListFragment () {
        // Required empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            _parentHabit = (Habit) getArguments().getSerializable("parentHabit");
            _userId = (String) getArguments().getSerializable("userId");
        }

        _parentHabit.setHabitEvents(new HabitEventList());
        _habitEventList = _parentHabit.getHabitEvents();

        // Get context
        Context context = getContext();

        try {
            // Get query
            Query query = getListOfHabitEventsFromFirebase(_userId);

            // Populate the list with existing items in the database
            FirestoreRecyclerOptions<HabitEvent> options = new FirestoreRecyclerOptions.Builder<HabitEvent>()
                    .setQuery(query, HabitEvent.class)
                    .build();


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
                    String id = extractMap.get("id").toString();

                    // create the new habit event from the snapshot data and add to local list
                    HabitEvent addHabitEvent = new HabitEvent(date.toDate(), comment, id);
                    Log.d(TAG, addHabitEvent.getId());
                    _habitEventList.addHabitEventLocally(addHabitEvent);
                }
            }

            // Set item adapter and habit event list
            _habitEventItemAdapter = new HabitEventItemAdapter(options, _parentHabit, _userId, _habitEventList, _fragment);
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
                openAddHabitEventDialogBox();
            }
        });

        // Initialize recycler view and return view
        initializeRecyclerView(layoutManager, view);
        return view;
    }

    /**
     * Gets the proper query string
     * @param userId name of the user performing the query
     * @return
     */
    @NonNull
    private Query getListOfHabitEventsFromFirebase(String userId) {
        // Query is made of username, habit name, and events
        Query query = _db.collection("Users")
                        .document(userId)
                        .collection("Habits")
                        .document(Long.toString(_parentHabit.getId()))
                        .collection("Events");
        return query;
    }

    /**
     * Initialize the recycler view
     * @param layoutManager Manager of the layout of the recycler view to initialize
     * @param view The layout of the recycler view to initialize
     */
    private void initializeRecyclerView(LinearLayoutManager layoutManager, View view) {
        // Find recycler view
        RecyclerView recyclerView = view.findViewById(R.id.habit_events_recycler_view);

        // Set needed elements
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(_habitEventItemAdapter);
        new ItemTouchHelper(_itemTouchHelperCallback).attachToRecyclerView(recyclerView);
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

    /**
     * Open the add habit event dialog
     */
    private void openAddHabitEventDialogBox() {
        // Create new AddHabitEventDialog and show it
        AddHabitEventDialog addHabitEventDialog = new AddHabitEventDialog();
        addHabitEventDialog.setTargetFragment(HabitEventListFragment.this, 1);
        addHabitEventDialog.show(getFragmentManager(), "AddHabitEventDialog");
    }

    /**
     * Add a new habit event to the database
     * @param date (Date) The date of the new event
     * @param comment (String) The comment of the new event
     */
    public void addHabitEventToDatabase(Date date, String comment) {
        _habitEventList.addHabitEventToDatabase(date, comment, Uri.EMPTY, _userId, _parentHabit);
    }

    /**
     * TODO: Complete this function, responsible for adding image to the database
     * @param image The image to add to the database
     * @param id The ID of the habit event corresponding to the image
     */
    private void addImageToDatabase(Uri image, UUID id) {
        // Get firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        // Create path for image
        String storageUrl = "img/" + _userId + "/" + _parentHabit.getId() + "/" + id;

        // Create reference with new path and attempt upload
        StorageReference imageStorageRef = storageReference.child(storageUrl);
        UploadTask uploadTask = imageStorageRef.putFile(image);

        // Handle upload success
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Data failed to be added." + e.toString());
            }
        });

        // Handle upload failure
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Data successfully added.");
            }
        });
    }

    /**
     * The implementation of the swipe to delete functionality below came from the following URL:
     * https://stackoverflow.com/questions/33985719/android-swipe-to-delete-recyclerview
     *
     * Name: Rahul Raina
     * Date: November 2, 2016
     */
    ItemTouchHelper.SimpleCallback _itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // Get the habit event view holder
            HabitEventItemAdapter.HabitEventViewHolder habitEventViewHolder = (HabitEventItemAdapter.HabitEventViewHolder) viewHolder;

            // Switch to buttons or no buttons view depending on swipe direction
            if (direction == ItemTouchHelper.LEFT) {
                habitEventViewHolder.setButtonView();
            } else if (direction == ItemTouchHelper.RIGHT){
                habitEventViewHolder.setNoButtonView();
            }

            // Alert item adapter that there was a change
            _habitEventItemAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
        }
    };

    /**
     * Update a habit event after it has been edited
     * @param newComment (String) The edited comment
     * @param newDate (Date) The edited date
     * @param pos (int) The position of the habit event in the list
     * @param id (String) The ID of the habit event to edit
     * @param viewHolder (HabitEventItemAdapter.HabitEventViewHolder) The view associated with current habit
     */
    public void updateAfterEdit(String newComment, Date newDate, int pos,String id, HabitEventItemAdapter.HabitEventViewHolder viewHolder) {
        _habitEventList.editHabitInDatabase(newComment, newDate, id, _userId, _parentHabit);
        viewHolder.setNoButtonView();
        _habitEventItemAdapter.notifyItemChanged(pos);
    }

    @Override
    public void onDestroyView() { super.onDestroyView();}
}
