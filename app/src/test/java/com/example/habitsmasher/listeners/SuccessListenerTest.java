package com.example.habitsmasher.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * This class hosts the unit tests for the SuccessListener listener
 *
 * The reference for testing listeners is the following url:
 * URL: https://stackoverflow.com/questions/18913827/how-to-test-listener-interface-is-called-within-android-unit-tests/19050763
 */
public class SuccessListenerTest {
    private static final String TAG = "Example tag";
    private static final String MSG = "Example message";
    private static final String TOAST_MSG = "Toast message";

    private SuccessListener _successListener;
    private SuccessListener _successListenerWithToast;

    @Before
    public void setUp() {
        _successListener = Mockito.spy(new SuccessListener(TAG, MSG));
        _successListenerWithToast = Mockito.spy(new SuccessListener(mock(Context.class),
                TAG, MSG, TOAST_MSG));
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
}
