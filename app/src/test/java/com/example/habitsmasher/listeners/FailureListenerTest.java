package com.example.habitsmasher.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * This class hosts the unit tests for the FailureListener listener
 *
 * The reference for testing listeners is the following url:
 * URL: https://stackoverflow.com/questions/18913827/how-to-test-listener-interface-is-called-within-android-unit-tests/19050763
 */
public class FailureListenerTest {
    private static final String TAG = "Example tag";
    private static final String MSG = "Example message";
    private static final String TOAST_MSG = "Toast message";

    private FailureListener _failureListener;
    private FailureListener _failureListenerWithToast;

    @Before
    public void setUp() {
        _failureListener = Mockito.spy(new FailureListener(TAG, MSG));
        _failureListenerWithToast = Mockito.spy(new FailureListener(mock(Context.class),
                TAG, MSG, TOAST_MSG));
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
}
