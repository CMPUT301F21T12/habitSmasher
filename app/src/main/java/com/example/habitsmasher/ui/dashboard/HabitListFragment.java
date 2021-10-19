package com.example.habitsmasher.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class HabitListFragment extends Fragment implements EditHabitFragment.EditHabitListener {
    private final HabitList _habitList = new HabitList();
    private final ArrayList<Habit> _habits = _habitList.getHabitList();
    private HabitItemAdapter _habitItemAdapter;
    private Button _editButton;
    private Button _deleteButton;
    private HabitListFragment _fragment = this;
    private CollectionReference collectionReference;
    private FirebaseFirestore _db;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Context context = getContext();
        _habitItemAdapter = new HabitItemAdapter(context, _habits, _fragment);
        _db = FirebaseFirestore.getInstance();
        collectionReference = _db.collection("Habits");

        LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                                                                    LinearLayoutManager.VERTICAL,
                                                                    false);

        View view = inflater.inflate(R.layout.fragment_habit_list, container, false);

        initializeRecyclerView(layoutManager, view);

        return view;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public void editHabit(String title, String reason, Date date, int pos) {
        HashMap<String, Habit> habitHashMap = new HashMap<>();
        habitHashMap.put(title, new Habit(title, reason, date));
        collectionReference.document(title).set(habitHashMap);
        Habit habit = _habits.get(pos);
        habit.setTitle(title);
        habit.setReason(reason);
        habit.setDate(date);
        _habitItemAdapter.notifyItemChanged(pos);
        _editButton.setVisibility(View.INVISIBLE);
        _deleteButton.setVisibility(View.INVISIBLE);
    }


}