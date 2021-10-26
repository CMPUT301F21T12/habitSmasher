package com.example.habitsmasher.ui.history;

import android.content.Context;
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
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class HabitEventListFragment extends Fragment {
    private static final String TAG = "HabitEventListFragment";

    private HabitEventItemAdapter _habitEventItemAdapter;
    private Habit _parentHabit;
    private User _user;
    private HabitEventList _habitEventList;

    FirebaseFirestore _db = FirebaseFirestore.getInstance();

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
        Context context = getContext();

        Query query = getListOfHabitEventsFromFirebase(_user.getUsername());

        try {
            // Populate the list with existing items in the database, query term is parent habit name
            FirestoreRecyclerOptions<HabitEvent> options = new FirestoreRecyclerOptions.Builder<HabitEvent>()
                    .setQuery(query, HabitEvent.class)
                    .build();
            _habitEventItemAdapter = new HabitEventItemAdapter(options, getActivity());
            _habitEventList = _parentHabit.getHabitEvents();
        }
        catch (Error e){
            // TODO: Add no events catch
        }
        // Inflate habit event list view
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        View view = inflater.inflate(R.layout.fragment_habit_event_list, container, false);

        // Add new habit fab button
        FloatingActionButton addHabitEventFab = view.findViewById(R.id.add_habit_event_fab);

        addHabitEventFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddHabitEventDialogBox();
            }
        });

        initializeRecyclerView(layoutManager, view);
        return view;
    }

    @NonNull
    private Query getListOfHabitEventsFromFirebase(String username) {
        Query query = _db.collection("Users")
                        .document(username)
                        .collection("Habits")
                        .document(_parentHabit.getTitle())
                        .collection("Events");
        return query;
    }

    private void initializeRecyclerView(LinearLayoutManager layoutManager, View view) {
        RecyclerView recyclerView = view.findViewById(R.id.habit_events_recycler_view);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(_habitEventItemAdapter);
        new ItemTouchHelper(_itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    @Override public void onStart() {
        super.onStart();
        _habitEventItemAdapter.startListening();
    }

    @Override public void onStop() {
        super.onStop();
        _habitEventItemAdapter.stopListening();
    }

    private void openAddHabitEventDialogBox() {
        AddHabitEventDialog addHabitEventDialog = new AddHabitEventDialog(_user.getUsername());
        addHabitEventDialog.show(getFragmentManager(), "AddHabitEventDialog");
    }


    public void addNewHabitEvent(HabitEvent habitEvent) { _habitEventList.addHabitEvent(habitEvent); }

    public void addHabitEventToDatabase(Date date, String comment, UUID id, String username) {
        final CollectionReference collectionReference = _db.collection("Users")
                                                        .document(username)
                                                        .collection("Habits")
                                                        .document(_parentHabit.getTitle())
                                                        .collection("Events");
        HashMap<String, Object> eventData = new HashMap<>();

        eventData.put("date", date);
        eventData.put("comment", comment);
        eventData.put("id", id);

        collectionReference
                .document(id.toString())
                .set(eventData)
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
