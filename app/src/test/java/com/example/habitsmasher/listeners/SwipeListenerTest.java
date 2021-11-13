package com.example.habitsmasher.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.habitsmasher.HabitList;
import com.example.habitsmasher.User;
import com.example.habitsmasher.ui.dashboard.HabitItemAdapter;
import com.example.habitsmasher.ui.dashboard.HabitListFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * This class hosts the unit tests for the SwipeListener listener
 *
 * The reference for testing listeners is the following url:
 * URL: https://stackoverflow.com/questions/18913827/how-to-test-listener-interface-is-called-within-android-unit-tests/19050763
 */
public class SwipeListenerTest {
    private static final int ID = 1;
    private static final int POSITION = 0;

    private SwipeListener _swipeListener;

    @Before
    public void setUp() {
        _swipeListener = Mockito.spy(new SwipeListener(mock(HabitListFragment.class)));
    }

    @Test
    public void swipeListener_validInput_expectNoErrors(){
        _swipeListener.onSwipeOptionClicked(ID, POSITION);
        //Here we can verify whether the methods "onSwipeOptionClicked" executed
        verify(_swipeListener, times(1)).onSwipeOptionClicked(ID, POSITION);
    }
}
