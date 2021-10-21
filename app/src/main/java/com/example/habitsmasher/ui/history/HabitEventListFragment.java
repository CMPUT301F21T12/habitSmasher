package com.example.habitsmasher.ui.history;

import android.content.Context;
import android.os.Bundle;
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
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class HabitEventListFragment extends Fragment {
    private HabitEventList _habitEventList = new HabitEventList();
    private HabitEventItemAdapter _habitEventItemAdapter;
    private Habit _parentHabit;
    FirebaseFirestore _db;

    public HabitEventListFragment (Habit parentHabit) {
        super();
        this._parentHabit = parentHabit;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param parentHabit (Habit): The habit for which the habit events are being displayed
     * @return A new instance of fragment HabitEventListFragment.
     */
    public static HabitEventListFragment newInstance(Habit parentHabit) {
        HabitEventListFragment fragment = new HabitEventListFragment(parentHabit);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = getContext();

        // Populate the list with existing items in the database, query term is parent habit name
        FirestoreRecyclerOptions<HabitEvent> options = new FirestoreRecyclerOptions.Builder<HabitEvent>()
                .setQuery(_db.collection(_parentHabit.getTitle()), HabitEvent.class)
                .build();
        _habitEventItemAdapter = new HabitEventItemAdapter(options, getActivity());

        // Inflate habit event list view
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        View view = inflater.inflate(R.layout.fragment_habit_event_list, container, false);

        // Add new habit fab button
        FloatingActionButton addHabitEventFab = view.findViewById(R.id.add_habit_event_fab);

        // TODO: Implement adding new habit event

        initializeRecyclerView(layoutManager, view);
        return view;
    }

    private void initializeRecyclerView(LinearLayoutManager layoutManager, View view) {
        RecyclerView recyclerView = view.findViewById(R.id.habit_events_recycler_view);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(_habitEventItemAdapter);
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
            _habitEventList.remove(viewHolder.getAdapterPosition());
            _habitEventItemAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
        }
    };

    @Override
    public void onDestroyView() { super.onDestroyView();}
}
