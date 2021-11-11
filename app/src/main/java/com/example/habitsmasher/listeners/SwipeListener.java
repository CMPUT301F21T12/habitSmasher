package com.example.habitsmasher.listeners;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitList;
import com.example.habitsmasher.R;
import com.example.habitsmasher.User;
import com.example.habitsmasher.ui.dashboard.EditHabitFragment;
import com.example.habitsmasher.ui.dashboard.HabitItemAdapter;
import com.example.habitsmasher.ui.dashboard.HabitListFragment;
import com.example.habitsmasher.ui.dashboard.RecyclerTouchListener;

public class SwipeListener implements RecyclerTouchListener.OnSwipeOptionsClickListener{
    private HabitItemAdapter _habitItemAdapter;
    private HabitListFragment _fragment;
    private HabitList _habitList;
    private User _user;

    public SwipeListener(HabitItemAdapter habitItemAdapter, HabitListFragment fragment, HabitList habitList, User user) {
        _habitItemAdapter = habitItemAdapter;
        _fragment = fragment;
        _habitList = habitList;
        _user = user;
    }

    @Override
    public void onSwipeOptionClicked(int viewID, int position) {
        switch (viewID){
            // if edit clicked
            case R.id.edit_button:
                EditHabitFragment editHabitFragment = new EditHabitFragment(position,
                        _habitItemAdapter._snapshots.get(position),
                        _fragment);
                editHabitFragment.show(_fragment.getFragmentManager(), "Edit Habit");
                break;
            // if delete clicked
            case R.id.delete_button:
                Habit habitToDelete = _habitItemAdapter._snapshots.get(position);
                _habitList.deleteHabit(_fragment.getActivity(), _user.getUsername(), habitToDelete, position);
                break;
        }
    }
}
