package com.example.habitsmasher.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitList;
import com.example.habitsmasher.R;

import java.util.ArrayList;
import java.util.Date;

public class HabitListFragment extends Fragment implements EditHabitFragment.EditHabitListener {
    private final HabitList _habitList = new HabitList();
    private final ArrayList<Habit> _habits = _habitList.getHabitList();
    private HabitItemAdapter _habitItemAdapter;

    Button _editButton;
    Button _deleteButton;

    // ensure that this fragment gets passed into dialog
    private HabitListFragment fragment = this;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Context context = getContext();
        _habitItemAdapter = new HabitItemAdapter(context, _habits);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                                                                    LinearLayoutManager.VERTICAL,
                                                                    false);

        View view = inflater.inflate(R.layout.fragment_habit_list, container, false);

        initializeRecyclerView(layoutManager, view);

        return view;
    }

    private void initializeRecyclerView(LinearLayoutManager layoutManager, View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_items);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(_habitItemAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
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
            int pos = viewHolder.getAdapterPosition();
            _editButton = habitView.findViewById(R.id.edit_button);
            _deleteButton = habitView.findViewById(R.id.delete_button);

            _editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditHabitFragment editHabitFragment = new EditHabitFragment(pos, _habits.get(pos), fragment);
                    editHabitFragment.show(getFragmentManager(), "Edit Habit");
                }
            });

            _deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // temp delete code for now
                    _habits.remove(pos);
                    _habitItemAdapter.notifyItemRemoved(pos);
                }
            });

            if (direction == ItemTouchHelper.LEFT) {
                _editButton.setVisibility(View.VISIBLE);
                _deleteButton.setVisibility(View.VISIBLE);
            }
            else if (direction == ItemTouchHelper.RIGHT) {
                _editButton.setVisibility(View.INVISIBLE);
                _deleteButton.setVisibility(View.INVISIBLE);
            }

            _habitItemAdapter.notifyItemChanged(pos);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void editHabit(String title, String reason, Date date, int pos) {
        Habit habit = _habits.get(pos);
        habit.setTitle(title);
        habit.setReason(reason);
        habit.setDate(date);
        _habitItemAdapter.notifyItemChanged(pos);
        _editButton.setVisibility(View.INVISIBLE);
        _deleteButton.setVisibility(View.INVISIBLE);
    }

}