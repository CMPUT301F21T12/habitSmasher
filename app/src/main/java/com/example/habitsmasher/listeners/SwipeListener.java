package com.example.habitsmasher.listeners;

import com.example.habitsmasher.Habit;
import com.example.habitsmasher.HabitList;
import com.example.habitsmasher.R;
import com.example.habitsmasher.User;
import com.example.habitsmasher.ui.dashboard.EditHabitFragment;
import com.example.habitsmasher.ui.dashboard.HabitItemAdapter;
import com.example.habitsmasher.ui.dashboard.HabitListFragment;
import com.example.habitsmasher.ui.dashboard.RecyclerTouchListener;
/**
 * This is the SwipeListener class
 * It implements the RecyclerTouchListener.OnSwipeOptionsClickListener
 * Its purpose is to perform the onSwipeOptionClicked listener actions
 */
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

    // note: add this to list fragment class once swipe is complete in habit event list
    protected void openEditDialogBox(int position) {
        EditHabitFragment editHabitFragment = new EditHabitFragment(position,
                _habitItemAdapter._snapshots.get(position),
                _fragment);
        editHabitFragment.show(_fragment.getFragmentManager(),
                "Edit Habit");
    }

    // add to list fragment class once swipe is fixed in habit events
    public void updateListAfterDelete(int position) {
        Habit habitToDelete = _habitItemAdapter._snapshots.get(position);
        _habitList.deleteHabit(_fragment.getActivity(),
                _user.getId(),
                habitToDelete,
                position);
    }
}
