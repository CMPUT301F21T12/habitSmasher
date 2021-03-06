package com.example.habitsmasher.listeners;

import com.example.habitsmasher.R;
import com.example.habitsmasher.ui.dashboard.HabitListFragment;
import com.example.habitsmasher.ui.dashboard.RecyclerTouchListener;
/**
 * This is the SwipeListener class
 * It implements the RecyclerTouchListener.OnSwipeOptionsClickListener
 * Its purpose is to perform the onSwipeOptionClicked listener actions
 *
 * @author Kaden Dreger
 */
public class SwipeListener implements RecyclerTouchListener.OnSwipeOptionsClickListener{
    private HabitListFragment _fragment;

    public SwipeListener(HabitListFragment fragment) {
        _fragment = fragment;
    }

    @Override
    public void onSwipeOptionClicked(int viewID, int position) {
        // edit and delete functionality below
        switch (viewID){
            // if edit button clicked
            case R.id.edit_button:
                _fragment.openEditDialogBox(position);
                break;
            // if delete button clicked
            case R.id.delete_button:
                _fragment.updateListAfterDelete(position);
                break;
        }
    }
}
