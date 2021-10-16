package com.example.habitsmasher.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitList;
import com.example.habitsmasher.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DashboardFragment extends Fragment implements HabitDialogListener{

    private static final String TAG = "DashboardFragment";
    private final HabitList _habitList = new HabitList();
    private final ArrayList<Habit> _habits = _habitList.getHabitList();
    private HabitItemAdapter _habitItemAdapter;
    FirebaseFirestore _db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Context context = getContext();
        _habitItemAdapter = new HabitItemAdapter(context, _habits);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                                                                    LinearLayoutManager.VERTICAL,
                                                                    false);

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        FloatingActionButton addHabitFab = view.findViewById(R.id.add_habit_fab);
        /**
         * When fab is pressed, method call to open dialog fragment.
         */
        addHabitFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHabitDialog();
            }
        });

        initializeRecyclerView(layoutManager, view);
        return view;
    }

    private void openHabitDialog() {
        AddHabitDialog addHabitDialog = new AddHabitDialog();
        addHabitDialog.setCancelable(true);
        addHabitDialog.setTargetFragment(DashboardFragment.this, 1);
        addHabitDialog.show(getFragmentManager(), "AddHabitDialog");
    }

    private void initializeRecyclerView(LinearLayoutManager layoutManager, View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_items);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(_habitItemAdapter);
        new ItemTouchHelper(_itemTouchHelperCallback).attachToRecyclerView(recyclerView);
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
            _habits.remove(viewHolder.getAdapterPosition());

            _habitItemAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
        }
    };

    @Override
    public void addNewHabit(String title, String reason, Date date) {
        Habit newHabit = new Habit(title, reason, date);
        _habits.add(newHabit);

        /**
         * Handling of adding a habit to firebase
         */
        _db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = _db.collection("Habits");
        HashMap<String, Habit> firebaseData = new HashMap<>();
        firebaseData.put(title, new Habit(title, reason, date));
        collectionReference
                .document(title)
                .set(firebaseData)
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
}