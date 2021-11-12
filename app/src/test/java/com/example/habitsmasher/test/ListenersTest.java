package com.example.habitsmasher.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.habitsmasher.DaysTracker;
import com.example.habitsmasher.HabitList;
import com.example.habitsmasher.User;
import com.example.habitsmasher.listeners.ClickListenerForCancel;
import com.example.habitsmasher.listeners.ClickListenerForDatePicker;
import com.example.habitsmasher.listeners.ClickListenerForDaysOfTheWeek;
import com.example.habitsmasher.listeners.FailureListener;
import com.example.habitsmasher.listeners.FailureListenerWithToast;
import com.example.habitsmasher.listeners.SuccessListener;
import com.example.habitsmasher.listeners.SuccessListenerWithToast;
import com.example.habitsmasher.listeners.SwipeListener;
import com.example.habitsmasher.ui.dashboard.HabitItemAdapter;
import com.example.habitsmasher.ui.dashboard.HabitListFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * This class hosts the unit tests for the listeners
 *
 * The reference for testing listeners is the following url:
 * URL: https://stackoverflow.com/questions/18913827/how-to-test-listener-interface-is-called-within-android-unit-tests/19050763
 */
public class ListenersTest {
    private static final String TAG = "Example tag";
    private static final String MSG = "Example message";
    private static final String TOAST_MSG = "Toast message";
    private static final String DAY = "MO";
    private static final int ID = 1;
    private static final int POSITION = 0;

    private ClickListenerForCancel _clickListenerForCancel;
    private ClickListenerForDatePicker _clickListenerForDatePicker;
    private ClickListenerForDaysOfTheWeek _clickListenerForDaysOfTheWeek;
    private FailureListener _failureListener;
    private FailureListenerWithToast _failureListenerWithToast;
    private SuccessListener _successListener;
    private SuccessListenerWithToast _successListenerWithToast;
    private SwipeListener _swipeListener;

    @Before
    public void setUp() {
        _clickListenerForCancel = Mockito.spy(new ClickListenerForCancel(mock(Dialog.class), TAG));
        _clickListenerForDatePicker = Mockito.spy(new ClickListenerForDatePicker
                (mock(FragmentManager.class), mock(TextView.class)));
        _clickListenerForDaysOfTheWeek = Mockito.spy(new ClickListenerForDaysOfTheWeek
                (mock(DaysTracker.class), DAY));
        _failureListener = Mockito.spy(new FailureListener(TAG, MSG));
        _failureListenerWithToast = Mockito.spy(new FailureListenerWithToast(mock(Context.class),
                TAG, MSG, TOAST_MSG));
        _successListener = Mockito.spy(new SuccessListener(TAG, MSG));
        _successListenerWithToast = Mockito.spy(new SuccessListenerWithToast(mock(Context.class),
                TAG, MSG, TOAST_MSG));
        _swipeListener = Mockito.spy(new SwipeListener(mock(HabitItemAdapter.class),
                mock(HabitListFragment.class), mock(HabitList.class), mock(User.class)));
    }

    @Test
    public void clickListenerForCancel_validInput_expectNoErrors(){
        View mockView = mock(View.class);
        _clickListenerForCancel.onClick(mockView);
        //Here we can verify whether the methods "onClick" executed
        verify(_clickListenerForCancel, times(1)).onClick(mockView);
    }

    @Test
    public void clickListenerForDatePicker_validInput_expectNoErrors(){
        View mockView = mock(View.class);
        _clickListenerForDatePicker.onClick(mockView);
        //Here we can verify whether the methods "onClick" executed
        verify(_clickListenerForDatePicker, times(1)).onClick(mockView);
    }

    @Test
    public void clickListenerForDaysOfTheWeek_validInput_expectNoErrors(){
        View mockView = mock(View.class);
        _clickListenerForDaysOfTheWeek.onClick(mockView);
        //Here we can verify whether the methods "onClick" executed
        verify(_clickListenerForDaysOfTheWeek, times(1)).onClick(mockView);
    }

    @Test
    public void failureListener_validInput_expectNoErrors(){
        Exception mockException = mock(Exception.class);
        _failureListener.onFailure(mockException);
        //Here we can verify whether the methods "onFailure" executed
        verify(_failureListener, times(1)).onFailure(mockException);
    }

    @Test
    public void failureListenerWithToast_validInput_expectNoErrors(){
        Exception mockException = mock(Exception.class);
        _failureListenerWithToast.onFailure(mockException);
        //Here we can verify whether the methods "onFailure" executed
        verify(_failureListenerWithToast, times(1)).onFailure(mockException);

    }

    @Test
    public void successListener_validInput_expectNoErrors(){
        Object mockObject = mock(Object.class);
        _successListener.onSuccess(mockObject);
        //Here we can verify whether the methods "onSuccess" executed
        verify(_successListener, times(1)).onSuccess(mockObject);
    }

    @Test
    public void successListenerWithToast_validInput_expectNoErrors(){
        Object mockObject = mock(Object.class);
        _successListenerWithToast.onSuccess(mockObject);
        //Here we can verify whether the methods "onSuccess" executed
        verify(_successListenerWithToast, times(1)).onSuccess(mockObject);
    }

    @Test
    public void swipeListener_validInput_expectNoErrors(){
        _swipeListener.onSwipeOptionClicked(ID, POSITION);
        //Here we can verify whether the methods "onSwipeOptionClicked" executed
        verify(_swipeListener, times(1)).onSwipeOptionClicked(ID, POSITION);
    }
}
