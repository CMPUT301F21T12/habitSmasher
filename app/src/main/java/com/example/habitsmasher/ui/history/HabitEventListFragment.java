package com.example.habitsmasher.ui.history;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
import com.example.habitsmasher.User;
import com.example.habitsmasher.ui.dashboard.HabitListFragment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * HabitEventListFragment class
 * Responsible for data and UI handling of the habit event list
 */
public class HabitEventListFragment extends Fragment {
    // Initialize variables
    private static final String TAG = "HabitEventListFragment";

    private HabitEventItemAdapter _habitEventItemAdapter;
    private Habit _parentHabit;
    private User _user;
    private HabitEventList _habitEventList;

    FirebaseFirestore _db = FirebaseFirestore.getInstance();

    /**
     * Default constructor
     * @param parentHabit The habit for which the habit event list is
     * @param parentUser The user for which the habit event list is
     */
    public HabitEventListFragment (Habit parentHabit, User parentUser) {
        super();
        this._parentHabit = parentHabit;
        this._user = parentUser;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param parentHabit (Habit): The habit for which the habit events are being displayed
     * @return A new instance of fragment HabitEventListFragment.
     */
    public static HabitEventListFragment newInstance(Habit parentHabit, User parentUser) {
        HabitEventListFragment fragment = new HabitEventListFragment(parentHabit, parentUser);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get context and query
        Context context = getContext();
        Query query = getListOfHabitEventsFromFirebase(_user.getUsername());

        try {
            // Populate the list with existing items in the database
            FirestoreRecyclerOptions<HabitEvent> options = new FirestoreRecyclerOptions.Builder<HabitEvent>()
                    .setQuery(query, HabitEvent.class)
                    .build();

            // Set item adapter and habit event list
            _habitEventItemAdapter = new HabitEventItemAdapter(options);
            _habitEventList = _parentHabit.getHabitEvents();
        }
        catch (Error e){
            // Try catch statement is needed so code doesn't break if there's no events yet, and thus no possible query
        }

        // Inflate habit event list view
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        View view = inflater.inflate(R.layout.fragment_habit_event_list, container, false);

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
     * @param username name of the user performing the query
     * @return
     */
    @NonNull
    private Query getListOfHabitEventsFromFirebase(String username) {
        // Query is made of username, habit name, and events
        Query query = _db.collection("Users")
                        .document(username)
                        .collection("Habits")
                        .document(_parentHabit.getTitle())
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

    @Override public void onStart() {
        super.onStart();

        // Enable live updates
        _habitEventItemAdapter.startListening();
    }

    @Override public void onStop() {
        super.onStop();

        // Stop live updates
        _habitEventItemAdapter.stopListening();
    }

    /**
     * Open the habit event dialog
     */
    private void openAddHabitEventDialogBox() {
        // Create new AddHabitEventDialog and show it
        AddHabitEventDialog addHabitEventDialog = new AddHabitEventDialog(_user.getUsername());
        addHabitEventDialog.setTargetFragment(HabitEventListFragment.this, 1);
        addHabitEventDialog.show(getFragmentManager(), "AddHabitEventDialog");
    }


    /**
     * Add a new habit event to the list
     * @param habitEvent The event to add to the list
     */
    public void addNewHabitEvent(HabitEvent habitEvent) {
        // If there were no previous habit events, initialize habit events list
        if (_habitEventList == null) {
            _parentHabit.setHabitEvents(new HabitEventList());
            this._habitEventList = _parentHabit.getHabitEvents();
        }

        // Add input event
        _habitEventList.addHabitEvent(habitEvent);
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
        String storageUrl = "img/" + _user.getUsername() + "/" + _parentHabit.getId() + "/" + id;

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
     * Add a new habit event to the database
     * @param date Date of the habit event
     * @param comment Comment of the new habit event
     * @param id ID of the new habit event
     * @param pictureUri Picture of the new habit event
     * @param username Username of the user adding the habit event
     */
    public void addHabitEventToDatabase(Date date, String comment, UUID id, Uri pictureUri, String username) {
        // Get reference to habit events collection
        final CollectionReference collectionReference = _db.collection("Users")
                                                        .document(username)
                                                        .collection("Habits")
                                                        .document(_parentHabit.getTitle())
                                                        .collection("Events");

        // Store data in a hash map
        HashMap<String, Object> eventData = new HashMap<>();
        eventData.put("date", date);
        eventData.put("comment", comment);
        eventData.put("id", id);

        // Set data in database
        collectionReference
                .document(id.toString())
                .set(eventData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    // Handle success
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Data successfully added.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    // Handle failure
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data failed to be added." + e.toString());
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
    ItemTouchHelper.SimpleCallback _itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // if the habit row is swiped to the left, remove it from the list and notify adapter
            _habitEventList.remove(viewHolder.getAdapterPosition());
            _habitEventItemAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
        }
    };

    @Override
    public void onDestroyView() { super.onDestroyView();}
}
